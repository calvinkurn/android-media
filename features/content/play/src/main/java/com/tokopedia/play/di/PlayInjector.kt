package com.tokopedia.play.di

import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.abstraction.base.app.BaseMainApplication

/**
 * Created by kenny.hadisaputra on 21/03/22
 */
object PlayInjector {

    private var customComponent: PlayComponent? = null

    fun get(activity: AppCompatActivity): PlayComponent = synchronized(this) {
        return customComponent ?: DaggerPlayComponent.factory()
            .create(
                baseAppComponent = (activity.applicationContext as BaseMainApplication).baseAppComponent,
                activity = activity,
            )
    }

    fun set(component: PlayComponent) = synchronized(this) {
        customComponent = component
    }
}
