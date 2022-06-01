package com.tokopedia.play.di

import android.content.Context
import com.tokopedia.abstraction.base.app.BaseMainApplication

/**
 * Created by kenny.hadisaputra on 21/03/22
 */
object PlayInjector {

    private var customComponent: PlayComponent? = null

    fun get(context: Context): PlayComponent = synchronized(this) {
        return customComponent ?: DaggerPlayComponent.builder()
            .baseAppComponent(
                (context.applicationContext as BaseMainApplication).baseAppComponent
            )
            .playModule(PlayModule(context))
            .build()
    }

    fun set(component: PlayComponent) = synchronized(this) {
        customComponent = component
    }
}