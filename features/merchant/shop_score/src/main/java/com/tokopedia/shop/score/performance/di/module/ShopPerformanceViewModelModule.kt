package com.tokopedia.shop.score.performance.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.shop.score.performance.di.scope.ShopPerformanceScope
import com.tokopedia.shop.score.performance.presentation.viewmodel.ShopPerformanceViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ShopPerformanceViewModelModule {

    @ShopPerformanceScope
    @Binds
    abstract fun bindViewModelShopPerformanceFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ShopPerformanceViewModel::class)
    abstract fun shopPerformanceViewModel(shopPerformanceViewModel: ShopPerformanceViewModel): ViewModel
}