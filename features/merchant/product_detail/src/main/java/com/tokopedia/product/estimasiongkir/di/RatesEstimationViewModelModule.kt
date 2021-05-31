package com.tokopedia.product.estimasiongkir.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.product.estimasiongkir.view.viewmodel.RatesEstimationBoeViewModel
import com.tokopedia.product.estimasiongkir.view.viewmodel.RatesEstimationDetailViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class RatesEstimationViewModelModule{

    @RatesEstimationScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(RatesEstimationDetailViewModel::class)
    internal abstract fun productInfoViewModel(viewModel: RatesEstimationDetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RatesEstimationBoeViewModel::class)
    internal abstract fun ratesBoeViewModel(viewModel: RatesEstimationBoeViewModel): ViewModel
}