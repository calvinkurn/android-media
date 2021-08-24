package com.tokopedia.exploreCategory.adapter

import com.tokopedia.exploreCategory.ui.viewholder.viewmodel.AffiliateProductCardVHViewModel
import com.tokopedia.exploreCategory.ui.viewholder.viewmodel.AffiliateShareVHViewModel
import com.tokopedia.exploreCategory.ui.viewholder.viewmodel.AffiliateShimmerVHViewModel

interface AffiliateAdapterTypeFactory {
    fun type(viewModel: AffiliateProductCardVHViewModel): Int
    fun type(viewModel: AffiliateShimmerVHViewModel): Int
    fun type(viewModel: AffiliateShareVHViewModel): Int
}
