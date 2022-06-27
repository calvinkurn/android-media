package com.tokopedia.media.editor.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.tokopedia.media.editor.ui.fragment.EditorDetailFragment
import com.tokopedia.media.editor.ui.fragment.EditorFragment

interface EditorFragmentProvider {
    fun editorFragment(): Fragment
    fun editorDetailFragment(): Fragment
}

internal class EditorFragmentProviderImpl constructor(
    private val fragmentManager: FragmentManager,
    private val classLoader: ClassLoader
) : EditorFragmentProvider {

    override fun editorFragment(): Fragment {
        return get(EditorFragment::class.java.name) as EditorFragment
    }

    override fun editorDetailFragment(): Fragment {
        return get(EditorDetailFragment::class.java.name) as EditorDetailFragment
    }

    private fun get(name: String): Fragment {
        val fragmentFactory = fragmentManager.fragmentFactory
        return fragmentFactory.instantiate(classLoader, name)
    }

}