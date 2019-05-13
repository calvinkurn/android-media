package com.tokopedia.groupchat.animation.modifiers;

import android.util.Log;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.tokopedia.groupchat.animation.Particle;

public class PopModifier implements ParticleModifier {

	private int mInitialValue;
	private int mFinalValue;
	private long mStartTime;
	private long mEndTime;
	private float mDuration;
	private float mValueIncrement;
	private Interpolator mInterpolator;

	public PopModifier(int initialValue, int finalValue, long startMilis, long endMilis, Interpolator interpolator) {
		mInitialValue = initialValue;
		mFinalValue = finalValue;
		mStartTime = startMilis;
		mEndTime = endMilis;
		mDuration = mEndTime - mStartTime;
		mValueIncrement = mFinalValue-mInitialValue;
		mInterpolator = interpolator;
	}

	public PopModifier(int initialValue, int finalValue, long startMilis, long endMilis) {
		this(initialValue, finalValue, startMilis, endMilis, new LinearInterpolator());
	}

	@Override
	public void apply(Particle particle, long miliseconds) {
		if (miliseconds < mStartTime) {
			Log.d("tev", miliseconds + " <");
		}
		else if (miliseconds > mEndTime) {
			Log.d("tev", miliseconds + " >");
		}
		else if (miliseconds == mEndTime){
			Log.d("tev=", particle.mCurrentX + " "+ particle.mCurrentY +" =");
		}
		else {
			Log.d("tev", miliseconds + " -");
		}		
	}

}
