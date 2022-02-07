package com.tokopedia.digital_product_detail.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.digital_product_detail.presentation.viewmodel.DigitalPDPDataPlanViewModel
import com.tokopedia.digital_product_detail.presentation.viewmodel.DigitalPDPPulsaViewModel
import com.tokopedia.digital_product_detail.presentation.viewmodel.DigitalPDPTokenListrikViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class DigitalPDPViewModelModule {

    @DigitalPDPScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(DigitalPDPDataPlanViewModel::class)
    internal abstract fun digitalPDPdataPlanViewModel(customViewModel: DigitalPDPDataPlanViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DigitalPDPPulsaViewModel::class)
    internal abstract fun digitalPDPPulsaViewModel(customViewModel: DigitalPDPPulsaViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DigitalPDPTokenListrikViewModel::class)
    internal abstract fun digitalPDPTokenListrikViewModel(customViewModel: DigitalPDPTokenListrikViewModel): ViewModel
}
