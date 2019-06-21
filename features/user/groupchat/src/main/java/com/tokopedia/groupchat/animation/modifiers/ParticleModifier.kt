package com.tokopedia.groupchat.animation.modifiers

import com.tokopedia.groupchat.animation.Particle

interface ParticleModifier {

    /**
     * modifies the specific value of a particle given the current miliseconds
     * @param particle
     * @param miliseconds
     */
    fun apply(particle: Particle, miliseconds: Long)

}
