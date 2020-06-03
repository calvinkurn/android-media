package com.tokopedia.topupbills.telco.view.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.topupbills.telco.view.viewmodel.DigitalTelcoOperatorViewModel
import com.tokopedia.topupbills.telco.view.viewmodel.DigitalTelcoEnquiryViewModel
import com.tokopedia.topupbills.telco.view.viewmodel.SharedProductTelcoViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by nabillasabbaha on 10/05/19.
 */
@Module
@DigitalTopupScope
abstract class DigitalTopupViewModelModule {

    @DigitalTopupScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(DigitalTelcoOperatorViewModel::class)
    internal abstract fun digitalCustomTelcoViewModel(operatorViewModel: DigitalTelcoOperatorViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SharedProductTelcoViewModel::class)
    internal abstract fun digitalProductSharedTelcoViewModel(sharedProductTelcoViewModel: SharedProductTelcoViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DigitalTelcoEnquiryViewModel::class)
    internal abstract fun digitalTelcoEnquiryViewModel(enquiryViewModel: DigitalTelcoEnquiryViewModel): ViewModel
}