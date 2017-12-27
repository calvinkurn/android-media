package com.tokopedia.core.onboarding.animation;

/**
 * Created by stevenfredian on 7/28/17.
 */

public class BounceInterpolator implements android.view.animation.Interpolator {
    private double mAmplitude = 1;
    private double mFrequency = 10;

    public BounceInterpolator(double amplitude, double frequency) {
        mAmplitude = amplitude;
        mFrequency = frequency;
    }

    public float getInterpolation(float time) {
        return (float) (-1 * Math.pow(Math.E, -time/ mAmplitude) *
                Math.cos(mFrequency * time) + 1);
    }

    public double getmAmplitude() {
        return mAmplitude;
    }

    public void setmAmplitude(double mAmplitude) {
        this.mAmplitude = mAmplitude;
    }

    public double getmFrequency() {
        return mFrequency;
    }

    public void setmFrequency(double mFrequency) {
        this.mFrequency = mFrequency;
    }
}
