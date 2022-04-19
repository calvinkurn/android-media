package com.tokopedia.review.feature.reputationhistory.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.review.feature.reputationhistory.view.viewmodel.SellerReputationViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class SellerReputationViewModelModule {

    @SellerReputationScope
    @Binds
    abstract fun bindViewModelSellerReputationFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(SellerReputationViewModel::class)
    abstract fun sellerReputationViewModel(sellerReputationViewModel: SellerReputationViewModel): ViewModel
}