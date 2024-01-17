package com.tokopedia.gamification.pdp.presentation.adapters

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.gamification.pdp.presentation.viewHolders.KetupatBenefitCouponItemVH
import com.tokopedia.gamification.pdp.presentation.viewHolders.KetupatBenefitCouponSlugItemVH
import com.tokopedia.gamification.pdp.presentation.viewHolders.KetupatTopBannerVH
import com.tokopedia.gamification.pdp.presentation.viewHolders.KetupatCrackBannerVH
import com.tokopedia.gamification.pdp.presentation.viewHolders.KetupatReferralBannerVH
import com.tokopedia.gamification.pdp.presentation.viewHolders.KetupatBenefitCouponVH
import com.tokopedia.gamification.pdp.presentation.viewHolders.KetupatBenefitCouponSlugVH
import com.tokopedia.gamification.pdp.presentation.viewHolders.KetupatProductRecommItemVH
import com.tokopedia.gamification.pdp.presentation.viewHolders.KetupatProductRecommVH
import com.tokopedia.gamification.pdp.presentation.viewHolders.KetupatRedirectionBannerVH
import com.tokopedia.gamification.pdp.presentation.viewHolders.viewModel.KetupatBenefitCouponItemVHModel
import com.tokopedia.gamification.pdp.presentation.viewHolders.viewModel.KetupatBenefitCouponSlugItemVHModel
import com.tokopedia.gamification.pdp.presentation.viewHolders.viewModel.KetupatTopBannerVHModel
import com.tokopedia.gamification.pdp.presentation.viewHolders.viewModel.KetupatCrackBannerVHModel
import com.tokopedia.gamification.pdp.presentation.viewHolders.viewModel.KetupatReferralBannerVHModel
import com.tokopedia.gamification.pdp.presentation.viewHolders.viewModel.KetupatBenefitCouponVHModel
import com.tokopedia.gamification.pdp.presentation.viewHolders.viewModel.KetupatBenefitCouponSlugVHModel
import com.tokopedia.gamification.pdp.presentation.viewHolders.viewModel.KetupatProductRecommItemVHmodel
import com.tokopedia.gamification.pdp.presentation.viewHolders.viewModel.KetupatProductRecommVHmodel
import com.tokopedia.gamification.pdp.presentation.viewHolders.viewModel.KetupatRedirectionBannerVHModel

class KetupatLandingAdapterTypeFactory(/*Listener if needed*/) : BaseAdapterTypeFactory(),
    KetupatLandingTypeFactory {

    override fun type(model: KetupatTopBannerVHModel): Int {
        return KetupatTopBannerVH.LAYOUT
    }

    override fun type(model: KetupatCrackBannerVHModel): Int {
        return KetupatCrackBannerVH.LAYOUT
    }

    override fun type(model: KetupatReferralBannerVHModel): Int {
        return KetupatReferralBannerVH.LAYOUT
    }

    override fun type(model: KetupatBenefitCouponVHModel): Int {
        return KetupatBenefitCouponVH.LAYOUT
    }

    override fun type(model: KetupatBenefitCouponItemVHModel): Int {
        return KetupatBenefitCouponItemVH.LAYOUT
    }

    override fun type(model: KetupatBenefitCouponSlugVHModel): Int {
        return KetupatBenefitCouponSlugVH.LAYOUT
    }

    override fun type(model: KetupatBenefitCouponSlugItemVHModel): Int {
        return KetupatBenefitCouponSlugItemVH.LAYOUT
    }

    override fun type(model: KetupatRedirectionBannerVHModel): Int {
        return KetupatRedirectionBannerVH.LAYOUT
    }

    override fun type(model: KetupatProductRecommVHmodel): Int {
        return KetupatProductRecommVH.LAYOUT
    }

    override fun type(model: KetupatProductRecommItemVHmodel): Int {
        return KetupatProductRecommItemVH.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            KetupatTopBannerVH.LAYOUT -> KetupatTopBannerVH(parent)
            KetupatCrackBannerVH.LAYOUT -> KetupatCrackBannerVH(parent)
            KetupatReferralBannerVH.LAYOUT -> KetupatReferralBannerVH(parent)
            KetupatBenefitCouponVH.LAYOUT -> KetupatBenefitCouponVH(parent)
            KetupatBenefitCouponSlugVH.LAYOUT -> KetupatBenefitCouponSlugVH(parent)
            KetupatRedirectionBannerVH.LAYOUT -> KetupatRedirectionBannerVH(parent)
            KetupatProductRecommItemVH.LAYOUT -> KetupatProductRecommItemVH(parent)
            KetupatBenefitCouponItemVH.LAYOUT -> KetupatBenefitCouponItemVH(parent)
            KetupatBenefitCouponSlugItemVH.LAYOUT -> KetupatBenefitCouponSlugItemVH(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}
