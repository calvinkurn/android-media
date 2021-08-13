package com.tokopedia.logisticaddaddress.di.addnewaddress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets.autocomplete_geocode.AutoCompleteBottomSheetViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class AutoCompleteBottomSheetViewModelsModule {

    @Binds
    @AddNewAddressScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(AutoCompleteBottomSheetViewModel::class)
    internal abstract fun bindAutoCompleteBottomSheetViewModel(viewModel: AutoCompleteBottomSheetViewModel) : ViewModel
}