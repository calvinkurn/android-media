package com.tokopedia.topupbills.telco.common.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.topupbills.telco.postpaid.viewmodel.DigitalTelcoEnquiryViewModel
import com.tokopedia.topupbills.telco.prepaid.viewmodel.SharedTelcoPrepaidViewModel
import com.tokopedia.topupbills.telco.common.viewmodel.SharedTelcoViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by nabillasabbaha on 10/05/19.
 */
@Module
@DigitalTelcoScope
abstract class DigitalTelcoViewModelModule {

    @DigitalTelcoScope
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