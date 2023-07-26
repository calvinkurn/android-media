package com.tokopedia.editor.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.tokopedia.editor.ui.fragment.image.placement.ImagePlacementFragment
import com.tokopedia.editor.ui.fragment.main.MainEditorFragment
import com.tokopedia.editor.ui.fragment.text.InputTextFragment

internal class EditorFragmentProviderImpl constructor(
    private val fragmentManager: FragmentManager,
    private val classLoader: ClassLoader
) : EditorFragmentProvider {

    // -- parent -- //

    override fun mainEditorFragment(): Fragment {
        return create(MainEditorFragment::class.java.name) as MainEditorFragment
    }

    override fun inputTextFragment(): Fragment {
        return create(InputTextFragment::class.java.name) as InputTextFragment
    }

    // -- image -- //

    override fun imagePlacementFragment(): Fragment {
        return create(ImagePlacementFragment::class.java.name) as ImagePlacementFragment
    }

    private fun create(name: String): Fragment {
        val fragmentFactory = fragmentManager.fragmentFactory
        return fragmentFactory.instantiate(classLoader, name)
    }
}
