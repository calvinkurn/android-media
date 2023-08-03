package com.tokopedia.tokofood.stub.purchase.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.tokofood.feature.purchase.purchasepage.di.TokoFoodPurchaseScope
import com.tokopedia.tokofood.stub.purchase.presentation.viewmodel.TokoFoodPurchaseConsentViewModelStub
import com.tokopedia.tokofood.stub.purchase.presentation.viewmodel.TokoFoodPurchaseViewModelOldStub
import com.tokopedia.tokofood.stub.purchase.presentation.viewmodel.TokoFoodPurchaseViewModelStub
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class TokoFoodPurchaseViewModelModuleStub {

    @TokoFoodPurchaseScope
    @Binds
    internal abstract fun bindViewModelFactoryStub(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @TokoFoodPurchaseScope
    @Binds
    @IntoMap
    @ViewModelKey(TokoFoodPurchaseViewModelStub::class)
    internal abstract fun tokoFoodPurchaseViewModelStub(viewModel: TokoFoodPurchaseViewModelStub): ViewModel

    @TokoFoodPurchaseScope
    @Binds
    @IntoMap
    @ViewModelKey(TokoFoodPurchaseViewModelOldStub::class)
    internal abstract fun tokoFoodPurchaseViewModelOldStub(viewModel: TokoFoodPurchaseViewModelOldStub): ViewModel

    @TokoFoodPurchaseScope
    @Binds
    @IntoMap
    @ViewModelKey(TokoFoodPurchaseConsentViewModelStub::class)
    internal abstract fun tokoFoodPurchaseConsentViewModelStub(viewModel: TokoFoodPurchaseConsentViewModelStub): ViewModel

}
