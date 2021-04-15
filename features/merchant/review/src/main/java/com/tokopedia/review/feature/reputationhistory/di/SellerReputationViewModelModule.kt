package com.tokopedia.review.feature.reputationhistory.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.review.feature.reputationhistory.view.viewmodel.ShopScoreReputationViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class SellerReputationViewModelModule {
    @SellerReputationScope
    @Binds
    abstract fun bindViewModelInboxReviewFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ShopScoreReputationViewModel::class)
    abstract fun shopScoreReputationViewModelViewModel(shopScoreReputationViewModel: ShopScoreReputationViewModel): ViewModel
}