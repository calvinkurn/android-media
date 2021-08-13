package com.tokopedia.travel.country_code.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.travel.country_code.presentation.viewmodel.PhoneCodePickerViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * @author by furqan on 23/12/2019
 */
@Module
abstract class TravelCountryCodeViewModelModule {

    @TravelCountryCodeScope
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(PhoneCodePickerViewModel::class)
    abstract fun phoneCodePickerViewModel(viewModel: PhoneCodePickerViewModel): ViewModel

}