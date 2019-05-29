package com.tokopedia.topupbills.telco.view.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.topupbills.telco.view.viewmodel.*
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
    @ViewModelKey(DigitalTelcoCustomViewModel::class)
    internal abstract fun digitalCustomTelcoViewModel(customViewModel: DigitalTelcoCustomViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DigitalTelcoProductViewModel::class)
    internal abstract fun digitalProductTelcoViewModel(productViewModel: DigitalTelcoProductViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SharedProductTelcoViewModel::class)
    internal abstract fun digitalProductSharedTelcoViewModel(sharedProductTelcoViewModel: SharedProductTelcoViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DigitalTelcoEnquiryViewModel::class)
    internal abstract fun digitalTelcoEnquiryViewModel(enquiryViewModel: DigitalTelcoEnquiryViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TelcoCatalogMenuDetailViewModel::class)
    internal abstract fun telcoCatalogMenuDetailViewModel(enquiryViewModel: TelcoCatalogMenuDetailViewModel): ViewModel

}