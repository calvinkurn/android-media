package com.tokopedia.groupchat.animation.initializers

import java.util.Random

import com.tokopedia.groupchat.animation.Particle

class SpeeddByComponentsInitializer(
        private val mMinSpeedX: Float,
        private val mMaxSpeedX: Float,
        private val mMinSpeedY: Float,
        private val mMaxSpeedY: Float
) : ParticleInitializer {

    override fun initParticle(p: Particle, r: Random) {
        p.mSpeedX = r.nextFloat() * (mMaxSpeedX - mMinSpeedX) + mMinSpeedX
        p.mSpeedY = r.nextFloat() * (mMaxSpeedY - mMinSpeedY) + mMinSpeedY
    }

}
