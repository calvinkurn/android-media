package com.tokopedia.affiliate.di

import android.content.Context
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.affiliate.ui.bottomsheet.AffiliatePromotionBottomSheet
import com.tokopedia.affiliate.ui.fragment.AffiliateHelpFragment
import com.tokopedia.affiliate.ui.fragment.AffiliateHomeFragment
import com.tokopedia.affiliate.ui.fragment.AffiliatePromoFragment
import com.tokopedia.affiliate.ui.fragment.registration.AffiliatePortfolioFragment
import com.tokopedia.affiliate.ui.fragment.registration.AffiliateTermsAndConditionFragment
import dagger.Component

@AffiliateScope
@Component(modules = [AffiliateModule::class, AffiliateVMModule::class], dependencies = [BaseAppComponent::class])
interface AffiliateComponent {

    @get:ApplicationContext
    val context: Context

    fun injectHomeFragment(affiliateHomeFragment: AffiliateHomeFragment)

    fun injectPromoFragment(affiliatePromoFragment: AffiliatePromoFragment)

    fun injectHelpFragment(affiliateHelpFragment: AffiliateHelpFragment)

    fun inject(affiliatePromotionBottomSheet: AffiliatePromotionBottomSheet)

    fun injectPortfolioFragment(affiliatePortfolioFragment: AffiliatePortfolioFragment)

    fun injectTermAndConditionFragment(affiliateTermsAndConditionFragment: AffiliateTermsAndConditionFragment)

}
