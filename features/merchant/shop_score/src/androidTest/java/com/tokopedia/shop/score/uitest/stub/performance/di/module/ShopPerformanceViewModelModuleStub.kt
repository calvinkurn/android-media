package com.tokopedia.shop.score.uitest.stub.performance.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.shop.score.performance.di.scope.ShopPerformanceScope
import com.tokopedia.shop.score.uitest.stub.performance.presentation.viewmodel.ShopPerformanceViewModelStub
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ShopPerformanceViewModelModuleStub {

    @ShopPerformanceScope
    @Binds
    abstract fun bindViewModelShopPerformanceFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ShopPerformanceViewModelStub::class)
    abstract fun shopPerformanceViewModel(shopPerformanceViewModelModuleStub: ShopPerformanceViewModelModuleStub): ViewModel
}