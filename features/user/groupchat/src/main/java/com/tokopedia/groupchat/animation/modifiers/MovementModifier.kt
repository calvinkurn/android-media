package com.tokopedia.groupchat.animation.modifiers

import android.util.Log

import com.tokopedia.groupchat.animation.HesitateInterpolator
import com.tokopedia.groupchat.animation.Particle

class MovementModifier(private val mInitialValue: Float, toLeft: Boolean) : ParticleModifier {
    private val mDuration: Long
    private val mValueIncrement: Float
    private var mInterpolator: HesitateInterpolator? = null

    init {
        mDuration = 5000
        mValueIncrement = 25f
        mInterpolator = if (toLeft) {
            HesitateInterpolator(-1)
        } else {
            HesitateInterpolator()
        }
    }

    override fun apply(particle: Particle, miliseconds: Long) {

        Log.d("steven miliseconds", "miliseconds: $miliseconds")
        if (miliseconds > 0 && miliseconds < mDuration) {
            val t = miliseconds * 20f / mDuration
            val interpolaterValue = mInterpolator!!.getInterpolation(t)
            val newScale = mInitialValue + mValueIncrement * interpolaterValue
            particle.mCurrentX = particle.mCurrentX + newScale
            Log.d("steven t", "t: $t")
            Log.d("steven interpolar", "interpolarValue: $interpolaterValue")
            Log.d("steven newScale", "newScale: $newScale")
            Log.d("steven increment", "increment: " + mValueIncrement * interpolaterValue)
            Log.d("steven mCurrentX", "mCurrentX: " + particle.mCurrentX)
        }
    }

}
