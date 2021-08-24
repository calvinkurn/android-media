package com.tokopedia.exploreCategory.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.basemvvm.viewmodel.ViewModelKey
import com.tokopedia.basemvvm.viewmodel.ViewModelProviderFactory
import com.tokopedia.exploreCategory.viewmodel.AffiliateHomeViewModel
import com.tokopedia.exploreCategory.viewmodel.AffiliatePromoViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class AffiliateVMModule {

    @AffiliateScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelProviderFactory: ViewModelProviderFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @AffiliateScope
    @ViewModelKey(AffiliateHomeViewModel::class)
    internal abstract fun affiliateViewModel(viewModel: AffiliateHomeViewModel): ViewModel

    @Binds
    @IntoMap
    @AffiliateScope
    @ViewModelKey(AffiliatePromoViewModel::class)
    internal abstract fun affiliatePromoViewModel(viewModel: AffiliatePromoViewModel): ViewModel


}
