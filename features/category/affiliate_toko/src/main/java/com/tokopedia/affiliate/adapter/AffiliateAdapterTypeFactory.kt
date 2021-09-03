package com.tokopedia.affiliate.adapter

import com.tokopedia.affiliate.ui.viewholder.viewmodel.*

interface AffiliateAdapterTypeFactory {
    fun type(viewModel: AffiliateProductCardVHViewModel): Int
    fun type(viewModel: AffiliateShimmerVHViewModel): Int
    fun type(viewModel: AffiliateShareVHViewModel): Int
    fun type(viewModel: AffiliatePromotionCardVHViewModel): Int
    fun type(viewModel: AffiliatePromotionErrorCardVHViewModel): Int
}
