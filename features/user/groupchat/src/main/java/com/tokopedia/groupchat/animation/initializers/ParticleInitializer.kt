package com.tokopedia.groupchat.animation.initializers

import com.tokopedia.groupchat.animation.Particle
import java.util.*

interface ParticleInitializer {

    fun initParticle(p: Particle, r: Random)

}
