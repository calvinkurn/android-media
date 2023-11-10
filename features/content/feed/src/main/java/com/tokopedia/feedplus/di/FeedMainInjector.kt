package com.tokopedia.feedplus.di

import android.content.Context
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.creation.common.upload.di.uploader.CreationUploaderComponentProvider

/**
 * Created By : Muhammad Furqan on 09/02/23
 */
object FeedMainInjector {
    private var component: FeedMainComponent? = null

    fun get(context: Context): FeedMainComponent = synchronized(this) {
        if (component == null) {
           component =  DaggerFeedMainComponent.builder()
                .baseAppComponent(
                    (context.applicationContext as BaseMainApplication).baseAppComponent
                )
                .creationUploaderComponent(
                    CreationUploaderComponentProvider.get(context)
                )
                .feedMainModule(FeedMainModule(context))
                .build()
        }

        return component!!
    }
}
