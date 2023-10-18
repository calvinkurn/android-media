package com.tokopedia.tokofood.feature.purchase.promopage.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.tokofood.feature.purchase.promopage.presentation.TokoFoodPromoViewModel
import com.tokopedia.tokofood.feature.purchase.promopage.presentation.MerchantTokoFoodPromoViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class TokoFoodPromoViewModelModule {

    @ActivityScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @ActivityScope
    @Binds
    @IntoMap
    @ViewModelKey(TokoFoodPromoViewModel::class)
    internal abstract fun bindViewModel(viewModel: TokoFoodPromoViewModel): ViewModel

    @ActivityScope
    @Binds
    @IntoMap
    @ViewModelKey(MerchantTokoFoodPromoViewModel::class)
    internal abstract fun bindViewModelOld(viewModel: MerchantTokoFoodPromoViewModel): ViewModel

}
