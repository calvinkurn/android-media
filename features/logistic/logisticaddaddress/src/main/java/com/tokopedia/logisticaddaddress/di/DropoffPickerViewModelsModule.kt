package com.tokopedia.logisticaddaddress.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.logisticaddaddress.features.dropoff_picker.DropoffPickerViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
@DropoffPickerScope
abstract class DropoffPickerViewModelsModule {

    @Binds
    @DropoffPickerScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(DropoffPickerViewModel::class)
    internal abstract fun bindDropoffViewModel(viewModel: DropoffPickerViewModel): ViewModel

}