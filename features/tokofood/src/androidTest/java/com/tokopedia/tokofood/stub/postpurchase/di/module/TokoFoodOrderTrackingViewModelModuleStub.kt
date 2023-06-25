package com.tokopedia.tokofood.stub.postpurchase.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.tokofood.feature.ordertracking.di.scope.TokoFoodOrderTrackingScope
import com.tokopedia.tokofood.feature.ordertracking.presentation.viewmodel.TokoFoodOrderTrackingViewModel
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
    @ViewModelKey(TokoFoodOrderTrackingViewModel::class)
    internal abstract fun tokoFoodOrderTrackingViewModelStub(viewModel: TokoFoodOrderTrackingViewModel): ViewModel
}
