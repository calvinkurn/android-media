package com.tokopedia.dropoff.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
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
    @ViewModelKey(com.tokopedia.dropoff.ui.dropoff_picker.DropoffPickerViewModel::class)
    internal abstract fun bindDropoffViewModel(viewModel: com.tokopedia.dropoff.ui.dropoff_picker.DropoffPickerViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(com.tokopedia.dropoff.ui.autocomplete.AutoCompleteViewModel::class)
    internal abstract fun bindAutoCompleteViewModel(viewModel: com.tokopedia.dropoff.ui.autocomplete.AutoCompleteViewModel): ViewModel

}