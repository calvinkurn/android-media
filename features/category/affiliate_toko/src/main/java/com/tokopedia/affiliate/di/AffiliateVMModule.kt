package com.tokopedia.affiliate.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.affiliate.viewmodel.AffiliateDatePickerBottomSheetViewModel
import com.tokopedia.affiliate.viewmodel.AffiliateEducationLandingViewModel
import com.tokopedia.affiliate.viewmodel.AffiliateEducationSearchArticleViewModel
import com.tokopedia.affiliate.viewmodel.AffiliateEducationSearchViewModel
import com.tokopedia.affiliate.viewmodel.AffiliateEducationSeeAllViewModel
import com.tokopedia.affiliate.viewmodel.AffiliateHomeViewModel
import com.tokopedia.affiliate.viewmodel.AffiliateLoginViewModel
import com.tokopedia.affiliate.viewmodel.AffiliatePortfolioViewModel
import com.tokopedia.affiliate.viewmodel.AffiliatePromoViewModel
import com.tokopedia.affiliate.viewmodel.AffiliatePromotionBSViewModel
import com.tokopedia.affiliate.viewmodel.AffiliatePromotionHistoryViewModel
import com.tokopedia.affiliate.viewmodel.AffiliateRecommendedProductViewModel
import com.tokopedia.affiliate.viewmodel.AffiliateRecyclerViewModel
import com.tokopedia.affiliate.viewmodel.AffiliateRegistrationSharedViewModel
import com.tokopedia.affiliate.viewmodel.AffiliateSSAShopViewModel
import com.tokopedia.affiliate.viewmodel.AffiliateTermsAndConditionViewModel
import com.tokopedia.affiliate.viewmodel.AffiliateTransactionDetailViewModel
import com.tokopedia.affiliate.viewmodel.AffiliateViewModel
import com.tokopedia.affiliate.viewmodel.WithdrawalDetailViewModel
import com.tokopedia.basemvvm.viewmodel.ViewModelKey
import com.tokopedia.basemvvm.viewmodel.ViewModelProviderFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class AffiliateVMModule {

    @AffiliateScope
    @Binds
    internal abstract fun bindViewModelFactory(
        viewModelProviderFactory: ViewModelProviderFactory
    ): ViewModelProvider.Factory

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
    @ViewModelKey(AffiliateEducationSearchViewModel::class)
    internal abstract fun affiliateEducationSearchViewModel(viewModel: AffiliateEducationSearchViewModel): ViewModel

    @Binds
    @IntoMap
    @AffiliateScope
    @ViewModelKey(AffiliateEducationSearchArticleViewModel::class)
    internal abstract fun affiliateEducationSearchArticleViewModel(viewModel: AffiliateEducationSearchArticleViewModel): ViewModel

    @Binds
    @IntoMap
    @AffiliateScope
    @ViewModelKey(AffiliateRecommendedProductViewModel::class)
    internal abstract fun affiliateRecommendedProductViewModel(
        viewModel: AffiliateRecommendedProductViewModel
    ): ViewModel

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
    @ViewModelKey(AffiliateTransactionDetailViewModel::class)
    internal abstract fun affiliateTransactionDetailVM(affiliateTransactionDetailViewModel: AffiliateTransactionDetailViewModel): ViewModel

//    @Binds
//    @IntoMap
//    @AffiliateScope
//    @ViewModelKey(AffiliateIncomeViewModel::class)
//    internal abstract fun affiliateIncomeViewModel(affiliateIncomeViewModel: AffiliateIncomeViewModel): ViewModel

    @Binds
    @IntoMap
    @AffiliateScope
    @ViewModelKey(AffiliatePromotionHistoryViewModel::class)
    internal abstract fun affiliatePromotionHistoryViewModel(viewModel: AffiliatePromotionHistoryViewModel): ViewModel

    @Binds
    @IntoMap
    @AffiliateScope
    @ViewModelKey(WithdrawalDetailViewModel::class)
    internal abstract fun affiliateWithdrawalDetailViewModel(viewModel: WithdrawalDetailViewModel): ViewModel

    @Binds
    @IntoMap
    @AffiliateScope
    @ViewModelKey(AffiliateRecyclerViewModel::class)
    internal abstract fun affiliateRecyclerViewModel(viewModel: AffiliateRecyclerViewModel): ViewModel

    @Binds
    @IntoMap
    @AffiliateScope
    @ViewModelKey(AffiliateDatePickerBottomSheetViewModel::class)
    internal abstract fun affiliateDatePickerViewModel(viewModel: AffiliateDatePickerBottomSheetViewModel): ViewModel

    @Binds
    @IntoMap
    @AffiliateScope
    @ViewModelKey(AffiliateRegistrationSharedViewModel::class)
    internal abstract fun affiliateRegistrationViewModel(viewModel: AffiliateRegistrationSharedViewModel): ViewModel

    @Binds
    @IntoMap
    @AffiliateScope
    @ViewModelKey(AffiliateEducationLandingViewModel::class)
    internal abstract fun affiliateEducationLandingViewModel(viewModel: AffiliateEducationLandingViewModel): ViewModel

    @Binds
    @IntoMap
    @AffiliateScope
    @ViewModelKey(AffiliateEducationSeeAllViewModel::class)
    internal abstract fun affiliateEducationSeeAllViewModel(viewModel: AffiliateEducationSeeAllViewModel): ViewModel

    @Binds
    @IntoMap
    @AffiliateScope
    @ViewModelKey(AffiliateSSAShopViewModel::class)
    internal abstract fun affiliateSSAShopViewModel(viewModel: AffiliateSSAShopViewModel): ViewModel

}
