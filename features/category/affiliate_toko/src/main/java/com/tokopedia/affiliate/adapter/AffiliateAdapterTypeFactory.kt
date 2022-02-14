package com.tokopedia.affiliate.adapter

import com.tokopedia.affiliate.ui.viewholder.viewmodel.*

interface AffiliateAdapterTypeFactory {
    fun type(viewModelShared: AffiliateSharedProductCardsModel): Int
    fun type(viewModelShared: AffiliatePerformaSharedProductCardsModel): Int
    fun type(viewModel: AffiliateShimmerModel): Int
    fun type(viewModel: AffiliateShareModel): Int
    fun type(viewModel: AffiliatePromotionCardModel): Int
    fun type(viewModel: AffiliatePromotionErrorCardModel): Int
    fun type(viewModel: AffiliatePortfolioUrlModel): Int
    fun type(viewModel:AffiliateHeaderModel): Int
    fun type(viewModel: AffiliatePortfolioButtonModel): Int
    fun type(viewModel: AffiliateTermsAndConditionModel): Int
    fun type(viewModel: AffiliateStaggeredShimmerModel): Int
    fun type(viewModel: AffiliateStaggeredPromotionCardModel): Int
    fun type(viewModel: AffiliateUserPerformanceModel): Int
    fun type(viewModel: AffiliateUserPerformanceListModel): Int
    fun type(viewModel: AffiliateDateFilterModel): Int
    fun type(viewModel: AffiliateNoPromoItemFoundModel): Int
    fun type(viewModel: AffiliateDataPlatformShimmerModel): Int
}
