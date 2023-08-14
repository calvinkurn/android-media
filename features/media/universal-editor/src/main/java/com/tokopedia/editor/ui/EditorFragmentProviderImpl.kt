package com.tokopedia.editor.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.tokopedia.editor.ui.main.fragment.image.main.ImageMainEditorFragment
import com.tokopedia.editor.ui.text.InputTextFragment
import com.tokopedia.editor.ui.main.fragment.video.VideoMainEditorFragment
import com.tokopedia.editor.ui.placement.PlacementImageFragment

internal class EditorFragmentProviderImpl constructor(
    private val fragmentManager: FragmentManager,
    private val classLoader: ClassLoader
) : EditorFragmentProvider {

    // -- parent -- //

    override fun inputTextFragment(): Fragment {
        return create(InputTextFragment::class.java.name) as InputTextFragment
    }

    // -- image -- //

    override fun imageMainEditorFragment(): Fragment {
        return create(ImageMainEditorFragment::class.java.name) as ImageMainEditorFragment
    }

    override fun placementImageFragment(): Fragment {
        return create(PlacementImageFragment::class.java.name) as PlacementImageFragment
    }

    // -- vod -- //

    override fun videoMainEditorFragment(): Fragment {
        return create(VideoMainEditorFragment::class.java.name) as VideoMainEditorFragment
    }

    private fun create(name: String): Fragment {
        val fragmentFactory = fragmentManager.fragmentFactory
        return fragmentFactory.instantiate(classLoader, name)
    }
}
