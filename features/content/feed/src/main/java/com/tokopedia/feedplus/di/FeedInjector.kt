package com.tokopedia.feedplus.di

import android.app.Activity
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.creation.common.upload.di.uploader.CreationUploaderComponentProvider

/**
 * Created by Jonathan Darwin on 25 April 2024
 */
object FeedInjector {

    private var component: FeedMainComponent? = null

    fun get(activity: Activity): FeedMainComponent {
        synchronized(this) {
            if (component == null) {
                component = DaggerFeedMainComponent.factory()
                    .build(
                        activityContext = activity,
                        appComponent = (activity.application as BaseMainApplication).baseAppComponent,
                        creationUploaderComponent = CreationUploaderComponentProvider.get(activity)
                    )
            }

            return component!!
        }
    }
}
