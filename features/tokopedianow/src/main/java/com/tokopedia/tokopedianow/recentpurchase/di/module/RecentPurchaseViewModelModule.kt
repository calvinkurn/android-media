package com.tokopedia.tokopedianow.recentpurchase.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.tokopedianow.recentpurchase.di.scope.RecentPurchaseScope
import com.tokopedia.tokopedianow.recentpurchase.presentation.viewmodel.TokoNowRecentPurchaseViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class RecentPurchaseViewModelModule {

    @Binds
    @RecentPurchaseScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(TokoNowRecentPurchaseViewModel::class)
    internal abstract fun tokoNowRecentPurchaseViewModel(viewModel: TokoNowRecentPurchaseViewModel): ViewModel
}