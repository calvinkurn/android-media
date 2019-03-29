package com.tokopedia.product.detail.estimasiongkir.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.product.detail.estimasiongkir.view.viewmodel.RatesEstimationDetailViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@RatesEstimationScope
@Module
abstract class RatesEstimationViewModelModule{

    @RatesEstimationScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(RatesEstimationDetailViewModel::class)
    internal abstract fun productInfoViewModel(viewModel: RatesEstimationDetailViewModel): ViewModel
}