package com.tokopedia.tokofood.purchase.purchasepage.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.tokofood.purchase.purchasepage.presentation.TokoFoodPurchaseViewModel
import com.tokopedia.tokofood.purchase.purchasepage.presentation.subview.TokoFoodPurchaseConsentViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class TokoFoodPurchaseViewModelModule {

    @ActivityScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @ActivityScope
    @Binds
    @IntoMap
    @ViewModelKey(TokoFoodPurchaseViewModel::class)
    internal abstract fun bindTokoFoodPurchaseViewModel(viewModel: TokoFoodPurchaseViewModel): ViewModel

    @ActivityScope
    @Binds
    @IntoMap
    @ViewModelKey(TokoFoodPurchaseConsentViewModel::class)
    internal abstract fun bindTokoFoodPurchaseConsentViewModel(viewModel: TokoFoodPurchaseConsentViewModel): ViewModel

}