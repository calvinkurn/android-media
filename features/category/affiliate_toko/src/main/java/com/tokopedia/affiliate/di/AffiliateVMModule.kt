package com.tokopedia.affiliate.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.basemvvm.viewmodel.ViewModelKey
import com.tokopedia.basemvvm.viewmodel.ViewModelProviderFactory
import com.tokopedia.affiliate.viewmodel.AffiliateHomeViewModel
import com.tokopedia.affiliate.viewmodel.AffiliatePortfolioViewModel
import com.tokopedia.affiliate.viewmodel.AffiliatePromoViewModel
import com.tokopedia.affiliate.viewmodel.AffiliatePromotionBSViewModel
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
    @ViewModelKey(AffiliatePromotionBSViewModel::class)
    internal abstract fun affiliatePromotionBSViewModel(viewModel: AffiliatePromotionBSViewModel): ViewModel

    @Binds
    @IntoMap
    @AffiliateScope
    @ViewModelKey(AffiliatePromoViewModel::class)
    internal abstract fun affiliatePromoViewModel(viewModel: AffiliatePromoViewModel): ViewModel


}
