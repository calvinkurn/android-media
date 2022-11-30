package com.tokopedia.tokopedianow.repurchase.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.tokopedianow.common.viewmodel.TokoNowProductRecommendationViewModel
import com.tokopedia.tokopedianow.repurchase.di.scope.RepurchaseScope
import com.tokopedia.tokopedianow.repurchase.presentation.viewmodel.TokoNowRepurchaseViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class RepurchaseViewModelModule {

    @Binds
    @RepurchaseScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(TokoNowRepurchaseViewModel::class)
    internal abstract fun tokoNowRepurchaseViewModel(viewModel: TokoNowRepurchaseViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TokoNowProductRecommendationViewModel::class)
    internal abstract fun tokoNowProductRecommendationViewModel(viewModel: TokoNowProductRecommendationViewModel): ViewModel
}
