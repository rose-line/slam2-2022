package fr.pgah.libgdx;

import java.util.Random;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Intro extends ApplicationAdapter {

  final int NB_SPRITES = 20;
  SpriteBatch batch;
  int longueurFenetre;
  int hauteurFenetre;
  Random generateurAleatoire;
  Texture img;
  int[] coordsX;
  int[] coordsY;
  boolean[] versLaDroite;
  boolean[] versLeHaut;
  double[] facteursTaille;
  int[] vitesses;
  float[] rotations;
  int[] vitessesRotation;

  @Override
  public void create() {
    generateurAleatoire = new Random();
    batch = new SpriteBatch();
    img = new Texture("sio.jpg");

    longueurFenetre = Gdx.graphics.getWidth();
    hauteurFenetre = Gdx.graphics.getHeight();
    initialiserFacteursTaille();
    initialiserVitesses();
    initialiserRotations();
    initialiserVitessesRotation();
    initialiserDirections();
    initialiserCoords();
  }

  private void initialiserCoords() {
    coordsX = new int[NB_SPRITES];
    coordsY = new int[NB_SPRITES];
    for (int i = 0; i < NB_SPRITES; i++) {
      coordsX[i] = generateurAleatoire.nextInt(longueurFenetre - calculerLongueurImg(i));
      coordsY[i] = generateurAleatoire.nextInt(hauteurFenetre - calculerHauteurImg(i));
    }
  }

  private int calculerLongueurImg(int index) {
    return (int) (img.getWidth() * facteursTaille[index]);
  }

  private int calculerHauteurImg(int index) {
    return (int) (img.getHeight() * facteursTaille[index]);
  }

  private void initialiserFacteursTaille() {
    facteursTaille = new double[NB_SPRITES];
    for (int i = 0; i < NB_SPRITES; i++) {
      facteursTaille[i] = 0.15;
    }
  }

  private void initialiserVitesses() {
    vitesses = new int[NB_SPRITES];
    for (int i = 0; i < NB_SPRITES; i++) {
      vitesses[i] = 1 + generateurAleatoire.nextInt(10);
    }
  }

  private void initialiserRotations() {
    rotations = new float[NB_SPRITES];
    for (int i = 0; i < NB_SPRITES; i++) {
      rotations[i] = 1;
    }
  }

  private void initialiserVitessesRotation() {
    vitessesRotation = new int[NB_SPRITES];
    for (int i = 0; i < NB_SPRITES; i++) {
      vitessesRotation[i] = 5 + generateurAleatoire.nextInt(21);
    }
  }

  private void initialiserDirections() {
    versLaDroite = new boolean[NB_SPRITES];
    versLeHaut = new boolean[NB_SPRITES];
    for (int i = 0; i < NB_SPRITES; i++) {
      versLaDroite[i] = generateurAleatoire.nextBoolean();
      versLeHaut[i] = generateurAleatoire.nextBoolean();
    }
  }

  @Override
  public void render() {
    reinitialiserArrierePlan();
    deplacer();
    pivoter();
    forcerAResterDansCadre();
    dessiner();
  }

  private void pivoter() {
    for (int i = 0; i < NB_SPRITES; i++) {
      rotations[i] = rotations[i] + vitessesRotation[i];
    }
  }

  private void reinitialiserArrierePlan() {
    // Gdx.gl.glClearColor(1, 0, 0, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
  }

  private void deplacer() {
    for (int i = 0; i < coordsX.length; i++) {
      if (versLaDroite[i]) {
        coordsX[i] = coordsX[i] + vitesses[i];
      } else {
        coordsX[i] = coordsX[i] - vitesses[i];
      }
      if (versLeHaut[i]) {
        coordsY[i] = coordsY[i] + vitesses[i];
      } else {
        coordsY[i] = coordsY[i] - vitesses[i];
      }
    }
  }

  private void dessiner() {
    batch.begin();
    for (int i = 0; i < coordsX.length; i++) {
      int longueur = calculerLongueurImg(i);
      int hauteur = calculerHauteurImg(i);
      batch.draw(img, coordsX[i], coordsY[i],
          longueur / 2, hauteur / 2, longueur, hauteur,
          1, 1, rotations[i],
          0, 0, img.getWidth(), img.getHeight(),
          false, false);
    }
    batch.end();
  }

  private void forcerAResterDansCadre() {
    for (int i = 0; i < coordsX.length; i++) {
      int longueur = calculerLongueurImg(i);
      int hauteur = calculerHauteurImg(i);
      // Gestion bordure droite
      if (coordsX[i] + longueur > longueurFenetre) {
        coordsX[i] = longueurFenetre - longueur;
        versLaDroite[i] = false;
      }

      // Gestion bordure gauche
      if (coordsX[i] < 0) {
        coordsX[i] = 0;
        versLaDroite[i] = true;
      }

      // Gestion bordures haute
      if (coordsY[i] + hauteur > hauteurFenetre) {
        coordsY[i] = hauteurFenetre - hauteur;
        versLeHaut[i] = false;
      }

      // Gestion bordure basse
      if (coordsY[i] < 0) {
        coordsY[i] = 0;
        versLeHaut[i] = true;
      }
    }
  }
}
