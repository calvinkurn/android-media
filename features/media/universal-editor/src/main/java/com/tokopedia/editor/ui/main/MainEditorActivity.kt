package com.tokopedia.editor.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.editor.di.ModuleInjector
import com.tokopedia.editor.ui.EditorFragmentProvider
import com.tokopedia.editor.ui.EditorFragmentProviderImpl
import javax.inject.Inject

class MainEditorActivity : BaseSimpleActivity() {

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        initInjector()
        supportFragmentManager.fragmentFactory = fragmentFactory
        super.onCreate(savedInstanceState)
    }

    override fun getNewFragment(): Fragment {
        return fragmentProvider().mainEditorFragment()
    }

    private fun fragmentProvider(): EditorFragmentProvider {
        return EditorFragmentProviderImpl(
            supportFragmentManager,
            applicationContext.classLoader
        )
    }

    private fun initInjector() {
        ModuleInjector
            .get(applicationContext)
            .inject(this)
    }
}
