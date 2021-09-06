package com.tokopedia.affiliate.adapter

import com.tokopedia.affiliate.ui.viewholder.viewmodel.*

interface AffiliateAdapterTypeFactory {
    fun type(viewModelShared: AffiliateSharedProductCardsModel): Int
    fun type(viewModel: AffiliateShimmerModel): Int
    fun type(viewModel: AffiliateShareModel): Int
    fun type(viewModel: AffiliatePromotionCardModel): Int
    fun type(viewModel: AffiliatePromotionErrorCardModel): Int
}
