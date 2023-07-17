package com.tokopedia.editor.di.module

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.tokopedia.abstraction.base.view.fragment.FragmentKey
import com.tokopedia.abstraction.base.view.fragment.TkpdFragmentFactory
import com.tokopedia.editor.ui.fragment.main.MainEditorFragment
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class FragmentEditorModule {

    @Binds
    internal abstract fun bindFragmentFactory(factory: TkpdFragmentFactory): FragmentFactory

    @Binds
    @IntoMap
    @FragmentKey(MainEditorFragment::class)
    internal abstract fun bindMainEditorFragment(fragment: MainEditorFragment): Fragment

}
