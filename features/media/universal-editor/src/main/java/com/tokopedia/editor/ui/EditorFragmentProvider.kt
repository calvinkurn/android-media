package com.tokopedia.editor.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.tokopedia.editor.ui.fragment.main.MainEditorFragment

interface EditorFragmentProvider {
    /**
     * Fragment for main page of universal editor.
     * This fragment contains the editor tool, container view, etc.
     */
    fun mainEditorFragment(): Fragment
}

internal class EditorFragmentProviderImpl constructor(
    private val fragmentManager: FragmentManager,
    private val classLoader: ClassLoader
) : EditorFragmentProvider {

    override fun mainEditorFragment(): Fragment {
        return create(MainEditorFragment::class.java.name) as MainEditorFragment
    }

    private fun create(name: String): Fragment {
        val fragmentFactory = fragmentManager.fragmentFactory
        return fragmentFactory.instantiate(classLoader, name)
    }
}
