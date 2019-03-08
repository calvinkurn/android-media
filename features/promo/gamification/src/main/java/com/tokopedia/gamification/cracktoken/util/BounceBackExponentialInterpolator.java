package com.tokopedia.gamification.cracktoken.util;

/**
 * The first value is the bounce amplitude. The higher value produces more pronounced bounces.
 * The second value is the frequency of the bounces. The higher value creates more wobbles during the animation time period.
 */

public class BounceBackExponentialInterpolator implements android.view.animation.Interpolator {
    private double mAmplitude = 1;
    private double mFrequency = 10;

    public BounceBackExponentialInterpolator(double amplitude, double frequency) {
        mAmplitude = amplitude;
        mFrequency = frequency;
    }

    public float getInterpolation(float time) {
        return (float) (-1 * Math.pow(Math.E, -time/ mAmplitude) *
                Math.cos(mFrequency * time) + 1);
    }
}
