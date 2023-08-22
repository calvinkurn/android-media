package com.tokopedia.stories.di

import android.content.Context
import com.tokopedia.abstraction.base.app.BaseMainApplication

object StoryInjector {

    private var customComponent: StoryComponent? = null

    fun get(context: Context): StoryComponent = synchronized(this) {
        return customComponent ?: DaggerStoryComponent.builder()
            .baseAppComponent(
                (context.applicationContext as BaseMainApplication).baseAppComponent,
            )
            .storyModule(StoryModule(context))
            .build()
    }

    fun set(component: StoryComponent) = synchronized(this) {
        customComponent = component
    }

}
