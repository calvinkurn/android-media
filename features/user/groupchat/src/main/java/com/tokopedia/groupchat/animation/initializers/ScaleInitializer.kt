package com.tokopedia.groupchat.animation.initializers

import java.util.Random

import com.tokopedia.groupchat.animation.Particle

class ScaleInitializer(private val mMinScale: Float, private val mMaxScale: Float) : ParticleInitializer {

    override fun initParticle(p: Particle, r: Random) {
        val scale = r.nextFloat() * (mMaxScale - mMinScale) + mMinScale
        p.mScale = scale
    }

}
