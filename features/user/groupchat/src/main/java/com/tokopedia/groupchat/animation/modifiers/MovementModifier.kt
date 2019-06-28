package com.tokopedia.groupchat.animation.modifiers

import com.tokopedia.groupchat.animation.HesitateInterpolator
import com.tokopedia.groupchat.animation.Particle

class MovementModifier(private val mInitialValue: Float, toLeft: Boolean) : ParticleModifier {
    private val mDuration: Long = 5000
    private val mValueIncrement: Float = 25f
    private var mInterpolator: HesitateInterpolator = if (toLeft) {
        HesitateInterpolator(-1)
    } else {
        HesitateInterpolator()
    }

    override fun apply(particle: Particle, miliseconds: Long) {
        if (miliseconds in 1..(mDuration - 1)) {
            val t = miliseconds * 20f / mDuration
            val interpolatedValue = mInterpolator.getInterpolation(t)
            val newScale = mInitialValue + mValueIncrement * interpolatedValue
            particle.mCurrentX = particle.mCurrentX + newScale
        }
    }

}
