package com.tokopedia.shop.score.stub.performance.di.module

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
abstract class ShopPerformanceViewModelModuleStub {
    @ShopPerformanceScope
    @Binds
    internal abstract fun bindViewModelFactoryStub(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ShopPerformanceViewModel::class)
    internal abstract fun shopPerformanceViewModelStub(viewModel: ShopPerformanceViewModel): ViewModel
}