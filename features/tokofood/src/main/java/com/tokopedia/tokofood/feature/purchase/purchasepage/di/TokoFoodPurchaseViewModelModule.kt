package com.tokopedia.tokofood.feature.purchase.purchasepage.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.TokoFoodPurchaseViewModel
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.TokoFoodPurchaseViewModelOld
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.subview.TokoFoodPurchaseConsentViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class TokoFoodPurchaseViewModelModule {

    @TokoFoodPurchaseScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @TokoFoodPurchaseScope
    @Binds
    @IntoMap
    @ViewModelKey(TokoFoodPurchaseViewModel::class)
    internal abstract fun bindTokoFoodPurchaseViewModel(viewModel: TokoFoodPurchaseViewModel): ViewModel

    @TokoFoodPurchaseScope
    @Binds
    @IntoMap
    @ViewModelKey(TokoFoodPurchaseViewModelOld::class)
    internal abstract fun bindTokoFoodPurchaseViewModelOld(viewModel: TokoFoodPurchaseViewModelOld): ViewModel

    @TokoFoodPurchaseScope
    @Binds
    @IntoMap
    @ViewModelKey(TokoFoodPurchaseConsentViewModel::class)
    internal abstract fun bindTokoFoodPurchaseConsentViewModel(viewModel: TokoFoodPurchaseConsentViewModel): ViewModel

}
