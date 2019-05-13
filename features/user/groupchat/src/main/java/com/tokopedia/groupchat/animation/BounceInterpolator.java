package com.tokopedia.groupchat.animation;

import android.view.animation.Interpolator;

import java.util.Random;

/**
 * @author : Steven 13/04/19
 */
public class BounceInterpolator implements Interpolator{

    private double mAmplitude = 1;
    private double mFrequency = 10;
    private Random random;
    private int left = 1;
    private int right = -1;
    private int direction;

    public BounceInterpolator(double amplitude, double frequency) {
        mAmplitude = amplitude;
        mFrequency = frequency;
        random = new Random();
        direction = random.nextBoolean()? left: right;
    }

    public float getInterpolation(float t) {
        float cons = t/4;
        float pembilang = (float) Math.sin(cons * Math.PI);
        float penyebut = (float) Math.pow(random.nextFloat()+1,cons);
        return direction * pembilang/penyebut;
    }
}
