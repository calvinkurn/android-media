package com.tokopedia.media.editor.di

import android.content.Context
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.media.editor.di.module.EditorColorProviderModule

object EditorInjector {
    private var component: EditorComponent? = null

    fun get(context: Context): EditorComponent = synchronized(this) {
        return component ?: DaggerEditorComponent.builder()
            .baseAppComponent((context.applicationContext as BaseMainApplication).baseAppComponent)
            .editorColorProviderModule(EditorColorProviderModule(context))
            .build()
    }

    fun set(component: EditorComponent) {
        this.component = component
    }
}
