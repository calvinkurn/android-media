package com.tokopedia.tokofood.stub.postpurchase.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.tokofood.feature.ordertracking.di.scope.TokoFoodOrderTrackingScope
import com.tokopedia.tokofood.stub.postpurchase.presentation.viewmodel.TokoFoodOrderTrackingViewModelStub
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class TokoFoodOrderTrackingViewModelModuleStub {

    @TokoFoodOrderTrackingScope
    @Binds
    internal abstract fun bindViewModelFactoryStub(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(TokoFoodOrderTrackingViewModelStub::class)
    internal abstract fun tokoFoodOrderTrackingViewModelStub(viewModel: TokoFoodOrderTrackingViewModelStub): ViewModel
}
