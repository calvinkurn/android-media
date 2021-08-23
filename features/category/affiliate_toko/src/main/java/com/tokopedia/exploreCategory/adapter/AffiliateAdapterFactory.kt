package com.tokopedia.exploreCategory.adapter

import android.view.View

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.exploreCategory.ui.viewholder.AffiliateProductShimmerCardItem
import com.tokopedia.exploreCategory.ui.viewholder.AffiliateVerticalProductCardItem
import com.tokopedia.exploreCategory.ui.viewholder.viewmodel.AffiliateProductCardVHViewModel
import com.tokopedia.exploreCategory.ui.viewholder.viewmodel.AffiliateProductShimmerVHViewModel

class AffiliateAdapterFactory()
    : BaseAdapterTypeFactory(), AffiliateAdapterTypeFactory {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            AffiliateVerticalProductCardItem.LAYOUT -> AffiliateVerticalProductCardItem(parent)
            AffiliateProductShimmerCardItem.LAYOUT -> AffiliateProductShimmerCardItem(parent)
            else -> super.createViewHolder(parent, type)
        }
    }

    override fun type(viewModel: AffiliateProductCardVHViewModel): Int {
        return AffiliateVerticalProductCardItem.LAYOUT
    }

    override fun type(viewModel: AffiliateProductShimmerVHViewModel): Int {
        return AffiliateProductShimmerCardItem.LAYOUT
    }
}
