package com.tokopedia.media.editor.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.tokopedia.media.editor.ui.fragment.AddTextFragment
import com.tokopedia.media.editor.ui.fragment.DetailEditorFragment
import com.tokopedia.media.editor.ui.fragment.EditorFragment

interface EditorFragmentProvider {
    fun editorFragment(): Fragment
    fun editorDetailFragment(): Fragment
    fun addTextFragment(): Fragment
}

internal class EditorFragmentProviderImpl constructor(
    private val fragmentManager: FragmentManager,
    private val classLoader: ClassLoader
) : EditorFragmentProvider {

    override fun editorFragment(): Fragment {
        return get(EditorFragment::class.java.name) as EditorFragment
    }

    override fun editorDetailFragment(): Fragment {
        return get(DetailEditorFragment::class.java.name) as DetailEditorFragment
    }

    override fun addTextFragment(): Fragment {
        return get(AddTextFragment::class.java.name) as AddTextFragment
    }

    private fun get(name: String): Fragment {
        val fragmentFactory = fragmentManager.fragmentFactory
        return fragmentFactory.instantiate(classLoader, name)
    }

}
