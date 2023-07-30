package com.tokopedia.editor.di.module

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.tokopedia.abstraction.base.view.fragment.FragmentKey
import com.tokopedia.abstraction.base.view.fragment.TkpdFragmentFactory
import com.tokopedia.editor.ui.main.fragment.image.main.ImageMainEditorFragment
import com.tokopedia.editor.ui.main.fragment.image.placement.ImagePlacementFragment
import com.tokopedia.editor.ui.main.fragment.video.VideoMainEditorFragment
import com.tokopedia.editor.ui.text.InputTextFragment
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class FragmentEditorModule {

    @Binds
    internal abstract fun bindFragmentFactory(factory: TkpdFragmentFactory): FragmentFactory

    @Binds
    @IntoMap
    @FragmentKey(ImageMainEditorFragment::class)
    internal abstract fun bindImageMainEditorFragment(fragment: ImageMainEditorFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(VideoMainEditorFragment::class)
    internal abstract fun bindVideoMainEditorFragment(fragment: VideoMainEditorFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(InputTextFragment::class)
    internal abstract fun bindInputTextFragment(fragment: InputTextFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(ImagePlacementFragment::class)
    internal abstract fun bindImagePlacementFragment(fragment: ImagePlacementFragment): Fragment

}
