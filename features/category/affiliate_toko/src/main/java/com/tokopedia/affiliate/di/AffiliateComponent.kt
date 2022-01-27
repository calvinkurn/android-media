package com.tokopedia.affiliate.di

import android.content.Context
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.affiliate.ui.activity.*
import com.tokopedia.affiliate.ui.bottomsheet.AffiliatePromotionBottomSheet
import com.tokopedia.affiliate.ui.fragment.*
import com.tokopedia.affiliate.ui.fragment.registration.AffiliateLoginFragment
import com.tokopedia.affiliate.ui.fragment.registration.AffiliatePortfolioFragment
import com.tokopedia.affiliate.ui.fragment.registration.AffiliateTermsAndConditionFragment
import com.tokopedia.affiliate.ui.fragment.AffiliateRecommendedProductFragment
import com.tokopedia.affiliate.ui.fragment.withdrawal.AffiliateSaldoWithdrawalDetailFragment
import dagger.Component

@AffiliateScope
@Component(modules = [AffiliateModule::class, AffiliateVMModule::class], dependencies = [BaseAppComponent::class])
interface AffiliateComponent {

    @get:ApplicationContext
    val context: Context

    fun injectActivity(affiliateActivity : AffiliateActivity)

    fun injectWithdrawalDetailActivity(affiliateAffiliateSaldoActivity : AffiliateSaldoWithdrawalDetailActivity)

    fun injectTransactionDetailActivity(affiliateTransactionDetailActivity: AffiliateTransactionDetailActivity)

    fun injectComponentActivity(affiliateComponentActivity : AffiliateComponentActivity)

    fun injectRegistrationActivity(affiliateRegistrationActivity : AffiliateRegistrationActivity)

    fun injectHomeFragment(affiliateHomeFragment: AffiliateHomeFragment)

    fun injectPromoFragment(affiliatePromoFragment: AffiliatePromoFragment)

    fun injectHelpFragment(affiliateHelpFragment: AffiliateHelpFragment)

    fun injectPromotionBottomSheet(affiliatePromotionBottomSheet: AffiliatePromotionBottomSheet)

    fun injectLoginFragment(affiliateLoginFragment : AffiliateLoginFragment)

    fun injectPortfolioFragment(affiliatePortfolioFragment: AffiliatePortfolioFragment)

    fun injectTermAndConditionFragment(affiliateTermsAndConditionFragment: AffiliateTermsAndConditionFragment)

    fun injectRecommendedProductFragment(affiliateRecommendedProductFragment: AffiliateRecommendedProductFragment)

    fun injectWithdrawalDetailFragment(affiliateSaldoWithdrawalDetailFragment: AffiliateSaldoWithdrawalDetailFragment)

    fun injectPromotionHistoryFragment(viewModel: AffiliatePromotionHistoryFragment)

}
