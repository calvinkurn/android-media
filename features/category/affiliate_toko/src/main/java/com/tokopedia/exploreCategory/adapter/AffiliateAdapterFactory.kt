package com.tokopedia.exploreCategory.adapter

import android.view.View

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.exploreCategory.ui.viewholder.AffiliateProductShimmerCardItemVH
import com.tokopedia.exploreCategory.ui.viewholder.AffiliateShareItemViewHolder
import com.tokopedia.exploreCategory.ui.viewholder.AffiliateVerticalProductCardItemVH
import com.tokopedia.exploreCategory.ui.viewholder.viewmodel.AffiliateProductCardVHViewModel
import com.tokopedia.exploreCategory.ui.viewholder.viewmodel.AffiliateShareVHViewModel
import com.tokopedia.exploreCategory.ui.viewholder.viewmodel.AffiliateShimmerVHViewModel

class AffiliateAdapterFactory()
    : BaseAdapterTypeFactory(), AffiliateAdapterTypeFactory {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            AffiliateVerticalProductCardItemVH.LAYOUT -> AffiliateVerticalProductCardItemVH(parent)
            AffiliateProductShimmerCardItemVH.LAYOUT -> AffiliateProductShimmerCardItemVH(parent)
            AffiliateShareItemViewHolder.LAYOUT -> AffiliateShareItemViewHolder(parent)
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
}
