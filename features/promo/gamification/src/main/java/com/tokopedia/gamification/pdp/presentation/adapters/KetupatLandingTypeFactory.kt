package com.tokopedia.gamification.pdp.presentation.adapters

import com.tokopedia.gamification.pdp.presentation.viewHolders.viewModel.KetupatBenefitCouponItemVHModel
import com.tokopedia.gamification.pdp.presentation.viewHolders.viewModel.KetupatBenefitCouponSlugItemVHModel
import com.tokopedia.gamification.pdp.presentation.viewHolders.viewModel.KetupatBenefitCouponSlugVHModel
import com.tokopedia.gamification.pdp.presentation.viewHolders.viewModel.KetupatBenefitCouponVHModel
import com.tokopedia.gamification.pdp.presentation.viewHolders.viewModel.KetupatCrackBannerVHModel
import com.tokopedia.gamification.pdp.presentation.viewHolders.viewModel.KetupatProductRecommVHmodel
import com.tokopedia.gamification.pdp.presentation.viewHolders.viewModel.KetupatRedirectionBannerVHModel
import com.tokopedia.gamification.pdp.presentation.viewHolders.viewModel.KetupatReferralBannerVHModel
import com.tokopedia.gamification.pdp.presentation.viewHolders.viewModel.KetupatTopBannerVHModel

interface KetupatLandingTypeFactory {

    fun type(model: KetupatTopBannerVHModel): Int

    fun type(model: KetupatCrackBannerVHModel): Int

    fun type(model: KetupatReferralBannerVHModel): Int

    fun type(model: KetupatBenefitCouponVHModel): Int

    fun type(model: KetupatBenefitCouponItemVHModel): Int

    fun type(model: KetupatBenefitCouponSlugVHModel): Int

    fun type(model: KetupatBenefitCouponSlugItemVHModel): Int

    fun type(model: KetupatRedirectionBannerVHModel): Int

    fun type(model: KetupatProductRecommVHmodel): Int
}
