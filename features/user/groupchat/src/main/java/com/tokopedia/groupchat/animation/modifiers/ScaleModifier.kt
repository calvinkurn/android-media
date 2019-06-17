package com.tokopedia.groupchat.animation.modifiers

import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import com.tokopedia.groupchat.animation.Particle

class ScaleModifier @JvmOverloads constructor(
        private val mInitialValue: Float,
        private val mFinalValue: Float,
        private val mStartTime: Long,
        private val mPeakTime: Long,
        private val mEndTime: Long

) : ParticleModifier {
    private val mDurationShrink: Long
    private val mDurationEnlarge: Long
    private val mValueIncrement: Float
    private val mInterpolator: Interpolator = LinearInterpolator()
    init {
        mDurationShrink  = mEndTime - mPeakTime
        mDurationEnlarge = mPeakTime - mStartTime
        mValueIncrement = mFinalValue - mInitialValue
    }

    override fun apply(particle: Particle, miliseconds: Long) {
        if (miliseconds < mStartTime) {
            particle.mScale = mInitialValue
        } else if (miliseconds > mEndTime) {
            particle.mScale = mInitialValue
        } else if (miliseconds < mPeakTime){
            val interpolaterdValue = mInterpolator.getInterpolation((miliseconds - mStartTime) * 1f / mDurationEnlarge)
            val newScale = mInitialValue + mValueIncrement * interpolaterdValue
            particle.mScale = newScale
        } else if (miliseconds > mPeakTime) {
            val interpolaterdValue = mInterpolator.getInterpolation((miliseconds - mPeakTime) * 1f / mDurationEnlarge)
            val newScale = mFinalValue - mValueIncrement * interpolaterdValue
            particle.mScale = newScale
        }
    }

}
