package com.tokopedia.groupchat.animation.initializers

import java.util.Random

import com.tokopedia.groupchat.animation.Particle

class RotationSpeedInitializer(private val mMinRotationSpeed: Float, private val mMaxRotationSpeed: Float) : ParticleInitializer {

    override fun initParticle(p: Particle, r: Random) {
        val rotationSpeed = r.nextFloat() * (mMaxRotationSpeed - mMinRotationSpeed) + mMinRotationSpeed
        p.mRotationSpeed = rotationSpeed
    }

}
