package com.tokopedia.groupchat.animation.initializers

import java.util.Random

import com.tokopedia.groupchat.animation.Particle

interface ParticleInitializer {

    fun initParticle(p: Particle, r: Random)

}
