package com.tokopedia.dropoff.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.dropoff.ui.autocomplete.AutoCompleteViewModel
import com.tokopedia.dropoff.ui.dropoff_picker.DropoffPickerViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class DropoffPickerViewModelsModule {

    @Binds
    @ActivityScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @ActivityScope
    @IntoMap
    @ViewModelKey(DropoffPickerViewModel::class)
    internal abstract fun bindDropoffViewModel(viewModel: DropoffPickerViewModel): ViewModel

    @Binds
    @ActivityScope
    @IntoMap
    @ViewModelKey(AutoCompleteViewModel::class)
    internal abstract fun bindAutoCompleteViewModel(viewModel: AutoCompleteViewModel): ViewModel
}
