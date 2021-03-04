package com.tokopedia.officialstore.official.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.officialstore.official.presentation.viewmodel.OfficialStoreHomeViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class OfficialStoreHomeViewModelModule {

    @OfficialStoreHomeScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(OfficialStoreHomeViewModel::class)
    internal abstract fun officialStoreHomeViewModel(viewModel: OfficialStoreHomeViewModel): ViewModel

}