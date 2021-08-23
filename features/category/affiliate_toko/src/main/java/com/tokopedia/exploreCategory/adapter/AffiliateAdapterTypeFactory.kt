package com.tokopedia.exploreCategory.adapter

import com.tokopedia.exploreCategory.ui.viewholder.viewmodel.AffiliateProductCardVHViewModel
import com.tokopedia.exploreCategory.ui.viewholder.viewmodel.AffiliateProductShimmerVHViewModel

interface AffiliateAdapterTypeFactory {
    fun type(viewModel: AffiliateProductCardVHViewModel): Int
    fun type(viewModel: AffiliateProductShimmerVHViewModel): Int
}