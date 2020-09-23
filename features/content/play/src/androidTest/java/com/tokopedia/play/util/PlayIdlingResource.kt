package com.tokopedia.play.util

import androidx.test.espresso.IdlingResource


/**
 * Created by mzennis on 15/09/20.
 * idling resource: artificial delays to the tests
 */
class ComponentIdlingResource(val play: PlayIdlingResource) : IdlingResource {

    override fun getName(): String = play.getName()

    private var callback: IdlingResource.ResourceCallback? = null

    override fun isIdleNow(): Boolean {
        val isIdle = play.idleState()
        if (isIdle) callback?.onTransitionToIdle()
        return isIdle
    }

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
        this.callback = callback
    }
}

interface PlayIdlingResource {
    fun getName(): String
    fun idleState(): Boolean
}