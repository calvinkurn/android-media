package com.tokopedia.stories.di

import android.content.Context
import com.tokopedia.abstraction.base.app.BaseMainApplication

object StoriesInjector {

    private var customComponent: StoriesComponent? = null

    fun get(context: Context): StoriesComponent = synchronized(this) {
        return customComponent ?: DaggerStoriesComponent.builder()
            .baseAppComponent(
                (context.applicationContext as BaseMainApplication).baseAppComponent,
            )
            .storiesModule(StoriesModule())
            .build()
    }

    fun set(component: StoriesComponent) = synchronized(this) {
        customComponent = component
    }

}
