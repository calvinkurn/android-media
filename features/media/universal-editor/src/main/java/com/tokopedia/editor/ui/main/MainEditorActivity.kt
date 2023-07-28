package com.tokopedia.editor.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentFactory
import com.tokopedia.editor.R
import com.tokopedia.editor.di.ModuleInjector
import com.tokopedia.editor.ui.EditorFragmentProvider
import com.tokopedia.editor.ui.EditorFragmentProviderImpl
import javax.inject.Inject

/**
 * A parent container of Universal Editor.
 *
 * This activity will manage all the ecosystem of Universal Editor, which will handling the
 * scope and area all editor tools. The universal editor module adopting a single activity,
 * which only contain a single entry point.
 *
 * To access the universal editor, please refer to use a built-in intent in [UniversalEditor] nor
 * you could access this page with this applink:
 *
 * @applink tokopedia-android-internal://global/universal-editor
 */
class MainEditorActivity : AppCompatActivity() {

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        initInjector()
        supportFragmentManager.fragmentFactory = fragmentFactory
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_editor)
        inflateFragment()
    }

    private fun inflateFragment() {
        val fragment = fragmentProvider().mainEditorFragment()

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
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
