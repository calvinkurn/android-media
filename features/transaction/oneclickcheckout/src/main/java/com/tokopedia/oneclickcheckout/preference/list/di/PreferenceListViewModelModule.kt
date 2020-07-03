package com.tokopedia.oneclickcheckout.preference.list.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.oneclickcheckout.preference.list.view.PreferenceListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class PreferenceListViewModelModule {

    @PreferenceListScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @PreferenceListScope
    @Binds
    @IntoMap
    @ViewModelKey(PreferenceListViewModel::class)
    internal abstract fun providesPreferenceListViewModel(viewModel: PreferenceListViewModel): ViewModel
}