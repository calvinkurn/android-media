package com.tokopedia.localizationchooseaddress.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.localizationchooseaddress.ui.bottomsheet.ChooseAddressViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ChooseAddressViewModelModule {

    @ChooseAddressScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @ChooseAddressScope
    @Binds
    @IntoMap
    @ViewModelKey(ChooseAddressViewModel::class)
    internal abstract fun providesChooseAddressViewModel(viewModel: ChooseAddressViewModel): ViewModel
}