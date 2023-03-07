package com.tokopedia.play.broadcaster.shorts.helper

import com.tokopedia.play.broadcaster.shorts.di.PlayShortsTestComponent

/**
 * Created By : Jonathan Darwin on December 12, 2022
 */
object PlayShortsInjector {

    private var customComponent: PlayShortsTestComponent? = null

    fun get(): PlayShortsTestComponent? = synchronized(this) {
        return customComponent
    }

    fun set(component: PlayShortsTestComponent) = synchronized(this) {
        customComponent = component
    }
}
