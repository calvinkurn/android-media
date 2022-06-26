package com.tokopedia.media.editor.ui.activity.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.tokopedia.media.editor.di.EditorInjector
import com.tokopedia.media.editor.ui.activity.BaseEditorActivity
import javax.inject.Inject

class EditorActivity : BaseEditorActivity() {

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        initInjector()
        supportFragmentManager.fragmentFactory = fragmentFactory
        super.onCreate(savedInstanceState)
    }

    override fun getNewFragment(): Fragment {
        return fragmentProvider().editorFragment()
    }

    private fun initInjector() {
        EditorInjector
            .get(applicationContext)
            .inject(this)
    }

}