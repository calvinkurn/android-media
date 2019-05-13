package com.tokopedia.groupchat.animation.initializers;

import java.util.Random;

import com.tokopedia.groupchat.animation.Particle;

public interface ParticleInitializer {

	void initParticle(Particle p, Random r);

}
