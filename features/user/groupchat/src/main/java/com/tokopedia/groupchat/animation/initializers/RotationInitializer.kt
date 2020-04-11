package com.tokopedia.groupchat.animation.initializers

import com.tokopedia.groupchat.animation.Particle

import java.util.Random

class RotationInitializer(private val mMinAngle: Int, private val mMaxAngle: Int) : ParticleInitializer {

    override fun initParticle(p: Particle, r: Random) {
        p.mInitialRotation =
                if (mMinAngle == mMaxAngle) mMinAngle.toFloat()
                else (r.nextInt(mMaxAngle - mMinAngle) + mMinAngle).toFloat()
    }

}
