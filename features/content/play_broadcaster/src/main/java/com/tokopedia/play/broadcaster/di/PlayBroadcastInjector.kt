package com.tokopedia.play.broadcaster.di

/**
 * Created By : Jonathan Darwin on April 12, 2023
 */
object PlayBroadcastInjector {

    private var customComponent: ActivityRetainedComponent? = null

    fun get(): ActivityRetainedComponent? = synchronized(this) {
        return customComponent
    }

    fun set(component: ActivityRetainedComponent) = synchronized(this) {
        customComponent = component
    }
}
