package com.tokopedia.affiliate.di

import android.content.Context
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.affiliate.ui.bottomsheet.AffiliatePromotionBottomSheet
import com.tokopedia.affiliate.ui.fragment.AffiliateHelpFragment
import com.tokopedia.affiliate.ui.fragment.AffiliateHomeFragment
import com.tokopedia.affiliate.ui.fragment.AffiliatePromoFragment
import com.tokopedia.affiliate.ui.fragment.AffiliateRecommendedProductFragment
import com.tokopedia.affiliate.viewmodel.AffiliateRecommendedProductViewModel
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

    fun injectRecommendedProductFragment(viewModel: AffiliateRecommendedProductFragment)

}
