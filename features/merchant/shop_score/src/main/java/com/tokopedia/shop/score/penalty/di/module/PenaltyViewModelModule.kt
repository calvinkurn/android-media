package com.tokopedia.shop.score.penalty.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.shop.score.penalty.di.scope.PenaltyScope
import com.tokopedia.shop.score.penalty.presentation.viewmodel.ShopPenaltyDetailViewModel
import com.tokopedia.shop.score.penalty.presentation.viewmodel.ShopPenaltyViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class PenaltyViewModelModule {

    @PenaltyScope
    @Binds
    abstract fun bindViewModelPenaltyFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ShopPenaltyViewModel::class)
    abstract fun penaltyViewModel(shopPenaltyViewModel: ShopPenaltyViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ShopPenaltyDetailViewModel::class)
    abstract fun penaltyDetailViewModel(shopPenaltyDetailViewModel: ShopPenaltyDetailViewModel): ViewModel
}