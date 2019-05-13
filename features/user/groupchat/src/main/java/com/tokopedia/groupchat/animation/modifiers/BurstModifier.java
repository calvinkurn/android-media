package com.tokopedia.groupchat.animation.modifiers;

import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.tokopedia.groupchat.animation.Particle;
import com.tokopedia.groupchat.animation.ParticleSystem;


public class BurstModifier implements ParticleModifier {

	private int mInitialValue;
	private int mFinalValue;
	private long mStartTime;
	private long mEndTime;
	private float explodeTime;
	private float mValueIncrement;
	private Interpolator mInterpolator;
	private boolean hasChanged = false;
	private ParticleSystem change;

	public BurstModifier(long endMilis, ParticleSystem drawable) {
		mStartTime = 1000;
		mEndTime = endMilis;
        explodeTime = mEndTime - mStartTime;
		mValueIncrement = mFinalValue-mInitialValue;
		change = drawable;
	}

	@Override
	public void apply(Particle particle, long miliseconds) {
//		if (miliseconds < mStartTime) {
//			particle.mAlpha = mInitialValue;
//		}
//		else if (miliseconds > mEndTime) {
//			particle.mAlpha = mFinalValue;
//		}
//		else {
//			float interpolaterdValue = mInterpolator.getInterpolation((miliseconds- mStartTime)*1f/mDuration);
//			int newAlphaValue = (int) (mInitialValue + mValueIncrement*interpolaterdValue);
//			particle.mAlpha = newAlphaValue;
//		}
//
//        if(miliseconds > explodeTime && hasChanged) {
//            hasChanged = true;
//            particle.mAlpha
//        }
	}

}
