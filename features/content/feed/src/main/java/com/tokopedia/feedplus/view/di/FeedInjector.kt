package com.tokopedia.feedplus.view.di

import android.content.Context
import com.tokopedia.abstraction.base.app.BaseMainApplication

/**
 * Created By : Jonathan Darwin on September 22, 2022
 */
object FeedInjector {

    private var customComponent: FeedContainerComponent? = null

    fun get(context: Context): FeedContainerComponent = synchronized(this) {
        return customComponent ?: DaggerFeedContainerComponent.builder()
            .baseAppComponent(
                (context.applicationContext as BaseMainApplication).baseAppComponent
            )
            .build()
    }

    fun set(component: FeedContainerComponent) = synchronized(this) {
        customComponent = component
    }
}
