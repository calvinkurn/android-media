package com.tokopedia.media.editor.di.module

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.tokopedia.media.editor.di.key.FragmentKey
import com.tokopedia.media.editor.ui.EditorFragmentFactory
import com.tokopedia.media.editor.ui.fragment.DetailEditorFragment
import com.tokopedia.media.editor.ui.fragment.EditorFragment
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class EditorFragmentModule {

    @Binds
    internal abstract fun bindFragmentFactory(factory: EditorFragmentFactory): FragmentFactory

    @Binds
    @IntoMap
    @FragmentKey(EditorFragment::class)
    internal abstract fun bindEditorFragment(fragment: EditorFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(DetailEditorFragment::class)
    internal abstract fun bindDetailEditorFragment(editorFragment: DetailEditorFragment): Fragment

}