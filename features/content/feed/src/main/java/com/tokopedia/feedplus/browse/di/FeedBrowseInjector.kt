package com.tokopedia.feedplus.browse.di

import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.abstraction.base.app.BaseMainApplication

/**
 * Created by Jonathan Darwin on 01 April 2024
 */
object FeedBrowseInjector {

    private var customComponent: FeedBrowseComponent? = null

    fun get(activity: AppCompatActivity): FeedBrowseComponent = synchronized(this) {
        return customComponent ?: DaggerFeedBrowseComponent.builder()
            .baseAppComponent((activity.applicationContext as BaseMainApplication).baseAppComponent)
            .build()
    }

    fun set(component: FeedBrowseComponent) = synchronized(this) {
        this.customComponent = component
    }
}
