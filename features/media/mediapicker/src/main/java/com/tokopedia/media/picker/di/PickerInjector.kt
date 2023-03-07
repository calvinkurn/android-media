package com.tokopedia.media.picker.di

import android.content.Context
import com.tokopedia.abstraction.base.app.BaseMainApplication

object PickerInjector {

    private var component: PickerComponent? = null

    fun get(context: Context): PickerComponent = synchronized(this) {
        return component?: DaggerPickerComponent.builder()
            .baseAppComponent((context.applicationContext as BaseMainApplication).baseAppComponent)
            .build()
    }

    fun set(component: PickerComponent) {
        this.component = component
    }

}