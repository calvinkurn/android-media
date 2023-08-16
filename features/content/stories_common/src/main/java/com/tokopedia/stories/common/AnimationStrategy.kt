package com.tokopedia.stories.common

/**
 * Created by kenny.hadisaputra on 15/08/23
 */
interface AnimationStrategy {

    fun shouldPlayAnimation(shopId: String): Boolean

    fun onAnimate(shopId: String)
}

class OneTimeAnimationStrategy : AnimationStrategy {

    private var mHasAnimate = false

    override fun shouldPlayAnimation(shopId: String): Boolean {
        return !mHasAnimate
    }

    override fun onAnimate(shopId: String) {
        mHasAnimate = true
    }
}

class NoAnimateAnimationStrategy : AnimationStrategy {

    override fun shouldPlayAnimation(shopId: String): Boolean {
        return false
    }

    override fun onAnimate(shopId: String) {}
}
