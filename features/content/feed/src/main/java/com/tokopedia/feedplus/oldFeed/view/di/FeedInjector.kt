package com.tokopedia.feedplus.oldFeed.view.di

import android.content.Context
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.creation.common.upload.di.uploader.CreationUploaderComponentProvider

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
            .creationUploaderComponent(
                CreationUploaderComponentProvider.get(context)
            )
            .feedContainerModule(FeedContainerModule(context))
            .build()
    }

    fun set(component: FeedContainerComponent) = synchronized(this) {
        customComponent = component
    }
}
