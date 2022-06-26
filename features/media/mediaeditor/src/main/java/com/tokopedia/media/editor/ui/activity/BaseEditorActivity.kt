package com.tokopedia.media.editor.ui.activity

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.media.editor.ui.EditorFragmentProvider
import com.tokopedia.media.editor.ui.EditorFragmentProviderImpl

abstract class BaseEditorActivity : BaseSimpleActivity() {

    protected open fun fragmentProvider(): EditorFragmentProvider {
        return EditorFragmentProviderImpl(
            supportFragmentManager,
            applicationContext.classLoader
        )
    }

}