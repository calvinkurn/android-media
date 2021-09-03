package com.tokopedia.affiliate.adapter

import android.view.View

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.interfaces.ShareButtonInterface
import com.tokopedia.affiliate.ui.viewholder.*
import com.tokopedia.affiliate.ui.viewholder.viewmodel.*

class AffiliateAdapterFactory(var shareButtonInterface: ShareButtonInterface? = null)
    : BaseAdapterTypeFactory(), AffiliateAdapterTypeFactory {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            AffiliateVerticalProductCardItemVH.LAYOUT -> AffiliateVerticalProductCardItemVH(parent)
            AffiliateProductShimmerCardItemVH.LAYOUT -> AffiliateProductShimmerCardItemVH(parent)
            AffiliateShareItemViewHolder.LAYOUT -> AffiliateShareItemViewHolder(parent, shareButtonInterface)
            AffiliatePromotionCardItemVH.LAYOUT -> AffiliatePromotionCardItemVH(parent)
            AffiliatePromotionErrorCardItemVH.LAYOUT -> AffiliatePromotionErrorCardItemVH(parent)
            else -> super.createViewHolder(parent, type)
        }
    }

    override fun type(viewModel: AffiliateProductCardVHViewModel): Int {
        return AffiliateVerticalProductCardItemVH.LAYOUT
    }

    override fun type(viewModel: AffiliateShimmerVHViewModel): Int {
        return AffiliateProductShimmerCardItemVH.LAYOUT
    }

    override fun type(viewModel: AffiliateShareVHViewModel): Int {
        return AffiliateShareItemViewHolder.LAYOUT
    }

    override fun type(viewModel: AffiliatePromotionCardVHViewModel): Int {
        return AffiliatePromotionCardItemVH.LAYOUT
    }

    override fun type(viewModel: AffiliatePromotionErrorCardVHViewModel): Int {
        return AffiliatePromotionErrorCardItemVH.LAYOUT
    }
}
