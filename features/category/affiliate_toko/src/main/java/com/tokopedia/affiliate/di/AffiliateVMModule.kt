package com.tokopedia.affiliate.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.affiliate.viewmodel.*
import com.tokopedia.basemvvm.viewmodel.ViewModelKey
import com.tokopedia.basemvvm.viewmodel.ViewModelProviderFactory
import com.tokopedia.affiliate.viewmodel.AffiliateHomeViewModel
import com.tokopedia.affiliate.viewmodel.AffiliatePromoViewModel
import com.tokopedia.affiliate.viewmodel.AffiliatePromotionBSViewModel
import com.tokopedia.affiliate.viewmodel.AffiliateRecommendedProductViewModel
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
    @ViewModelKey(AffiliateViewModel::class)
    internal abstract fun affiliateViewModel(viewModel: AffiliateViewModel): ViewModel

    @Binds
    @IntoMap
    @AffiliateScope
    @ViewModelKey(AffiliateHomeViewModel::class)
    internal abstract fun affiliateHomeViewModel(viewModel: AffiliateHomeViewModel): ViewModel

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

    @Binds
    @IntoMap
    @AffiliateScope
    @ViewModelKey(AffiliateRecommendedProductViewModel::class)
    internal abstract fun affiliateRecommendedProductViewModel(viewModel: AffiliateRecommendedProductViewModel): ViewModel

    @Binds
    @IntoMap
    @AffiliateScope
    @ViewModelKey(AffiliateLoginViewModel::class)
    internal abstract fun affiliateLoginViewModel(viewModel: AffiliateLoginViewModel): ViewModel
    @Binds
    @IntoMap
    @AffiliateScope
    @ViewModelKey(AffiliatePortfolioViewModel::class)
    internal abstract fun affiliatePortfolioViewModel(viewModel: AffiliatePortfolioViewModel): ViewModel

    @Binds
    @IntoMap
    @AffiliateScope
    @ViewModelKey(AffiliateTermsAndConditionViewModel::class)
    internal abstract fun affiliateTermsAndConditionViewModel(viewModel: AffiliateTermsAndConditionViewModel): ViewModel

    @Binds
    @IntoMap
    @AffiliateScope
    @ViewModelKey(AffiliatePromotionHistoryViewModel::class)
    internal abstract fun affiliatePromotionHistoryViewModel(viewModel: AffiliatePromotionHistoryViewModel): ViewModel


}
