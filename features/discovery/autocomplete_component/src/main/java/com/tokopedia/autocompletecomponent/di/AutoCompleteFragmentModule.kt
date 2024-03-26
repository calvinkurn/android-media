package com.tokopedia.autocompletecomponent.di

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.tokopedia.abstraction.base.view.fragment.FragmentKey
import com.tokopedia.abstraction.base.view.fragment.TkpdFragmentFactory
import com.tokopedia.autocompletecomponent.unify.AutoCompleteFragment
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class AutoCompleteFragmentModule {

    @Binds
    @AutoCompleteScope
    internal abstract fun provideAutoCompleteFragmentFactory(fragmentFactory: TkpdFragmentFactory): FragmentFactory

    @Binds
    @AutoCompleteScope
    @IntoMap
    @FragmentKey(AutoCompleteFragment::class)
    internal abstract fun provideAutoCompleteFragment(autoCompleteFragment: AutoCompleteFragment): Fragment
}
