package com.tokopedia.purchase_platform.features.one_click_checkout.preference.list.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.list.view.PreferenceListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@PreferenceListScope
@Module
abstract class PreferenceListViewModelModule {

    @PreferenceListScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(PreferenceListViewModel::class)
    internal abstract fun providesPreferenceListViewModel(viewModel: PreferenceListViewModel): ViewModel
}