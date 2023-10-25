package com.tokopedia.stories.creation.di

import android.content.Context
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.creation.common.upload.di.uploader.CreationUploaderComponentProvider

/**
 * Created By : Jonathan Darwin on October 12, 2023
 */
object StoriesCreationInjector {

    private var component: StoriesCreationComponent? = null

    fun get(context: Context): StoriesCreationComponent = synchronized(this) {
        if (component == null) {
            component = DaggerStoriesCreationComponent.builder()
                .baseAppComponent((context.applicationContext as BaseMainApplication).baseAppComponent)
                .creationUploaderComponent(CreationUploaderComponentProvider.get(context))
                .storiesCreationModule(StoriesCreationModule(context))
                .build()
        }

        return component!!
    }

    fun set(component: StoriesCreationComponent) {
        this.component = component
    }
}
