package com.tokopedia.topupbills.telco.view.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.topupbills.telco.view.viewmodel.SharedTelcoViewModel
import com.tokopedia.topupbills.telco.view.viewmodel.DigitalTelcoEnquiryViewModel
import com.tokopedia.topupbills.telco.view.viewmodel.SharedTelcoPrepaidViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by nabillasabbaha on 10/05/19.
 */
@Module
abstract class DigitalTopupViewModelModule {

    @DigitalTopupScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(SharedTelcoViewModel::class)
    internal abstract fun digitalCustomTelcoViewModel(viewModel: SharedTelcoViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SharedTelcoPrepaidViewModel::class)
    internal abstract fun digitalProductSharedTelcoViewModel(sharedTelcoPrepaidViewModel: SharedTelcoPrepaidViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DigitalTelcoEnquiryViewModel::class)
    internal abstract fun digitalTelcoEnquiryViewModel(enquiryViewModel: DigitalTelcoEnquiryViewModel): ViewModel
}