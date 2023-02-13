package com.tokopedia.affiliate.adapter

import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateCommisionDividerItemModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateCommisionThickDividerItemModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateCommissionItemModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateDataPlatformShimmerModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateDateFilterModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateEduCategoryChipModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateEducationArticleRVUiModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateEducationArticleTopicRVUiModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateEducationArticleTopicUiModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateEducationArticleUiModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateEducationBannerUiModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateEducationEventRVUiModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateEducationEventUiModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateEducationLearnUiModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateEducationSeeAllUiModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateEducationSocialRVUiModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateEducationSocialUiModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateEducationTutorialRVUiModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateEducationTutorialUiModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateHeaderModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateNoPromoItemFoundModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliatePerformaSharedProductCardsModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliatePerformanceChipModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliatePerformanceChipRVModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliatePortfolioButtonModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliatePortfolioUrlModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateProductCardMetricsModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliatePromotionCardModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliatePromotionErrorCardModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliatePromotionShopModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateSSAShopUiModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateShareModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateSharedProductCardsModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateShimmerModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateStaggeredPromotionCardModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateStaggeredShimmerModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateTermsAndConditionModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateTrafficCardModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateTransactionHistoryItemModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateUserPerformanceListModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateUserPerformanceModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateWithdrawalTitleItemModel

interface AffiliateAdapterTypeFactory {
    fun type(viewModelShared: AffiliateSharedProductCardsModel): Int
    fun type(viewModelShared: AffiliatePerformaSharedProductCardsModel): Int
    fun type(viewModel: AffiliateShimmerModel): Int
    fun type(viewModel: AffiliateShareModel): Int
    fun type(viewModel: AffiliatePromotionCardModel): Int
    fun type(viewModel: AffiliatePromotionShopModel): Int
    fun type(viewModel: AffiliatePromotionErrorCardModel): Int
    fun type(viewModel: AffiliatePortfolioUrlModel): Int
    fun type(viewModel: AffiliateHeaderModel): Int
    fun type(viewModel: AffiliatePortfolioButtonModel): Int
    fun type(viewModel: AffiliateTermsAndConditionModel): Int
    fun type(viewModel: AffiliateStaggeredShimmerModel): Int
    fun type(viewModel: AffiliateStaggeredPromotionCardModel): Int
    fun type(viewModel: AffiliateUserPerformanceModel): Int
    fun type(viewModel: AffiliateUserPerformanceListModel): Int
    fun type(viewModel: AffiliateDateFilterModel): Int
    fun type(viewModel: AffiliateNoPromoItemFoundModel): Int
    fun type(viewModel: AffiliateDataPlatformShimmerModel): Int
    fun type(viewModel: AffiliateTransactionHistoryItemModel): Int
    fun type(viewModel: AffiliateCommissionItemModel): Int
    fun type(viewModel: AffiliateCommisionDividerItemModel): Int
    fun type(viewModel: AffiliateCommisionThickDividerItemModel): Int
    fun type(viewModel: AffiliateWithdrawalTitleItemModel): Int
    fun type(viewModel: AffiliateTrafficCardModel): Int
    fun type(viewModel: AffiliateProductCardMetricsModel): Int
    fun type(viewModel: AffiliatePerformanceChipRVModel): Int
    fun type(viewModel: AffiliatePerformanceChipModel): Int
    fun type(viewModel: AffiliateEducationBannerUiModel): Int
    fun type(viewModel: AffiliateEducationArticleTopicRVUiModel): Int
    fun type(viewModel: AffiliateEducationArticleTopicUiModel): Int
    fun type(viewModel: AffiliateEducationEventRVUiModel): Int
    fun type(viewModel: AffiliateEducationEventUiModel): Int
    fun type(viewModel: AffiliateEducationArticleRVUiModel): Int
    fun type(viewModel: AffiliateEducationArticleUiModel): Int
    fun type(viewModel: AffiliateEducationTutorialRVUiModel): Int
    fun type(viewModel: AffiliateEducationTutorialUiModel): Int
    fun type(viewModel: AffiliateEducationSocialRVUiModel): Int
    fun type(viewModel: AffiliateEducationSocialUiModel): Int
    fun type(viewModel: AffiliateEducationLearnUiModel): Int
    fun type(viewModel: AffiliateEducationSeeAllUiModel): Int
    fun type(viewModel: AffiliateEduCategoryChipModel): Int
    fun type(viewModel: AffiliateSSAShopUiModel): Int
}
