package com.tokopedia.editor.di

import android.content.Context
import com.tokopedia.abstraction.base.app.BaseMainApplication

object ModuleInjector {

    private var component: UniversalEditorComponent? = null

    fun get(context: Context): UniversalEditorComponent = synchronized(this) {
        val appComponent = (context.applicationContext as BaseMainApplication)
            .baseAppComponent

        return component ?: DaggerUniversalEditorComponent.builder()
            .baseAppComponent(appComponent)
            .build()
    }

    fun set(component: UniversalEditorComponent) {
        this.component = component
    }
}
