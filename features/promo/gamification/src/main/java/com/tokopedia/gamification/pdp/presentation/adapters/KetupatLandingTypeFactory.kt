package com.tokopedia.gamification.pdp.presentation.adapters

import com.tokopedia.gamification.pdp.presentation.viewHolders.viewModel.KetupatTopBannerVHModel
import com.tokopedia.gamification.pdp.presentation.viewHolders.viewModel.KetupatCrackBannerVHModel
import com.tokopedia.gamification.pdp.presentation.viewHolders.viewModel.KetupatReferralBannerVHModel
import com.tokopedia.gamification.pdp.presentation.viewHolders.viewModel.KetupatBenefitCouponVHModel
import com.tokopedia.gamification.pdp.presentation.viewHolders.viewModel.KetupatBenefitCouponSlugVHModel
import com.tokopedia.gamification.pdp.presentation.viewHolders.viewModel.KetupatRedirectionBannerVHModel


interface KetupatLandingTypeFactory{

    fun type(model: KetupatTopBannerVHModel): Int

    fun type(model: KetupatCrackBannerVHModel): Int

    fun type(model: KetupatReferralBannerVHModel): Int

    fun type(model: KetupatBenefitCouponVHModel): Int

    fun type(model: KetupatBenefitCouponSlugVHModel): Int

    fun type(model: KetupatRedirectionBannerVHModel): Int
}
