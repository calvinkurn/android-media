package com.tokopedia.media.editor.di

import android.content.Context
import com.tokopedia.abstraction.base.app.BaseMainApplication

object EditorInjector {
    private var component: EditorComponent? = null

    fun get(context: Context): EditorComponent = synchronized(this) {
        return component ?: DaggerEditorComponent.builder()
            .baseAppComponent((context.applicationContext as BaseMainApplication).baseAppComponent)
            .build()
    }

    fun set(component: EditorComponent) {
        this.component = component
    }
}