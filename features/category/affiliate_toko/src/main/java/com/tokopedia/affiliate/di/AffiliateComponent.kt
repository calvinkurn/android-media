package com.tokopedia.affiliate.di

import android.content.Context
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.affiliate.ui.activity.AffiliateActivity
import com.tokopedia.affiliate.ui.activity.AffiliateComponentActivity
import com.tokopedia.affiliate.ui.activity.AffiliateRegistrationActivity
import com.tokopedia.affiliate.ui.bottomsheet.AffiliatePortfolioSocialMediaBottomSheet
import com.tokopedia.affiliate.ui.bottomsheet.AffiliatePromotionBottomSheet
import com.tokopedia.affiliate.ui.fragment.registration.AffiliateLoginFragment
import com.tokopedia.affiliate.ui.fragment.registration.AffiliatePortfolioFragment
import com.tokopedia.affiliate.ui.fragment.registration.AffiliateTermsAndConditionFragment
import com.tokopedia.affiliate.ui.fragment.AffiliateRecommendedProductFragment
import com.tokopedia.affiliate.ui.activity.AffiliateSaldoWithdrawalDetailActivity
import com.tokopedia.affiliate.ui.activity.AffiliateTransactionDetailActivity
import com.tokopedia.affiliate.ui.bottomsheet.AffiliateBottomDatePicker
import com.tokopedia.affiliate.ui.bottomsheet.AffiliateRecylerBottomSheet
import com.tokopedia.affiliate.ui.fragment.AffiliateHelpFragment
import com.tokopedia.affiliate.ui.fragment.AffiliateHomeFragment
import com.tokopedia.affiliate.ui.fragment.AffiliatePromoFragment
import com.tokopedia.affiliate.ui.fragment.AffiliatePromotionHistoryFragment
import com.tokopedia.affiliate.ui.fragment.AffiliateTransactionDetailFragment
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

    fun injectRecyclerBottomSheet(affiliateRecyclerBottomSheet: AffiliateRecylerBottomSheet)

    fun injectSocialMediaBottomSheet(affiliateSocialMediaBottomSheet: AffiliatePortfolioSocialMediaBottomSheet)

    fun injectLoginFragment(affiliateLoginFragment : AffiliateLoginFragment)

    fun injectPortfolioFragment(affiliatePortfolioFragment: AffiliatePortfolioFragment)

    fun injectTermAndConditionFragment(affiliateTermsAndConditionFragment: AffiliateTermsAndConditionFragment)

    fun injectRecommendedProductFragment(affiliateRecommendedProductFragment: AffiliateRecommendedProductFragment)

    fun injectWithdrawalDetailFragment(affiliateSaldoWithdrawalDetailFragment: AffiliateSaldoWithdrawalDetailFragment)

    fun injectPromotionHistoryFragment(viewModel: AffiliatePromotionHistoryFragment)

    fun injectWithdrawalInfoFragment(affiliateTransactionDetailFragment: AffiliateTransactionDetailFragment)

    fun injectDateFilterBottomSheet(affiliateDateFilterBottomSheet: AffiliateBottomDatePicker)

}
