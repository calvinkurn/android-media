package com.tokopedia.media.editor.base

import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.media.editor.ui.EditorFragmentProvider
import com.tokopedia.media.editor.ui.EditorFragmentProviderImpl

abstract class BaseEditorActivity : BaseSimpleActivity() {

    abstract fun initViewModel()
    abstract fun initBundle(savedInstanceState: Bundle?)
    abstract fun initInjector()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
        initBundle(savedInstanceState)
    }

    protected open fun fragmentProvider(): EditorFragmentProvider {
        return EditorFragmentProviderImpl(
            supportFragmentManager,
            applicationContext.classLoader
        )
    }

}