package com.tokopedia.content.common.producttag.di

/**
 * Created By : Jonathan Darwin on October 03, 2022
 */
object ContentProductTagTestInjector {

    private var customComponent: ContentProductTagTestComponent? = null

    fun get(): ContentProductTagTestComponent? = synchronized(this) {
        return customComponent
    }

    fun set(component: ContentProductTagTestComponent) = synchronized(this) {
        customComponent = component
    }
}