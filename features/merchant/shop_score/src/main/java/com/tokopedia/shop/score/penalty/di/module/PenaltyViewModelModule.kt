package com.tokopedia.shop.score.penalty.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.shop.score.penalty.presentation.PenaltyViewModel
import com.tokopedia.shop.score.performance.di.scope.ShopPerformanceScope
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class PenaltyViewModelModule {

    @ShopPerformanceScope
    @Binds
    abstract fun bindViewModelPenaltyFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(PenaltyViewModel::class)
    abstract fun penaltyViewModel(penaltyViewModel: PenaltyViewModel): ViewModel
}