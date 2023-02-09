package com.tokopedia.affiliate.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.interfaces.AddSocialInterface
import com.tokopedia.affiliate.interfaces.AffiliateDatePickerRangeChangeInterface
import com.tokopedia.affiliate.interfaces.AffiliateEduCategoryChipClick
import com.tokopedia.affiliate.interfaces.AffiliateEducationBannerClickInterface
import com.tokopedia.affiliate.interfaces.AffiliateEducationEventArticleClickInterface
import com.tokopedia.affiliate.interfaces.AffiliateEducationLearnClickInterface
import com.tokopedia.affiliate.interfaces.AffiliateEducationSeeAllCardClickInterface
import com.tokopedia.affiliate.interfaces.AffiliateEducationSocialCTAClickInterface
import com.tokopedia.affiliate.interfaces.AffiliateEducationTopicTutorialClickInterface
import com.tokopedia.affiliate.interfaces.AffiliateInfoClickInterfaces
import com.tokopedia.affiliate.interfaces.AffiliatePerformaClickInterfaces
import com.tokopedia.affiliate.interfaces.AffiliatePerformanceChipClick
import com.tokopedia.affiliate.interfaces.PortfolioClickInterface
import com.tokopedia.affiliate.interfaces.PortfolioUrlTextUpdateInterface
import com.tokopedia.affiliate.interfaces.ProductClickInterface
import com.tokopedia.affiliate.interfaces.PromotionClickInterface
import com.tokopedia.affiliate.interfaces.ShareButtonInterface
import com.tokopedia.affiliate.ui.custom.AffiliateBottomNavBarInterface
import com.tokopedia.affiliate.ui.viewholder.AffiliateCommisionDivderItemVH
import com.tokopedia.affiliate.ui.viewholder.AffiliateCommisionThickDivderItemVH
import com.tokopedia.affiliate.ui.viewholder.AffiliateCommissionDetailsItemVH
import com.tokopedia.affiliate.ui.viewholder.AffiliateDataCardShimmerItemVH
import com.tokopedia.affiliate.ui.viewholder.AffiliateDateFilterVH
import com.tokopedia.affiliate.ui.viewholder.AffiliateEduCategoryChipVH
import com.tokopedia.affiliate.ui.viewholder.AffiliateEducationArticleRVVH
import com.tokopedia.affiliate.ui.viewholder.AffiliateEducationArticleTopicRVVH
import com.tokopedia.affiliate.ui.viewholder.AffiliateEducationArticleTopicVH
import com.tokopedia.affiliate.ui.viewholder.AffiliateEducationArticleVH
import com.tokopedia.affiliate.ui.viewholder.AffiliateEducationBannerItemVH
import com.tokopedia.affiliate.ui.viewholder.AffiliateEducationEventRVVH
import com.tokopedia.affiliate.ui.viewholder.AffiliateEducationEventVH
import com.tokopedia.affiliate.ui.viewholder.AffiliateEducationLearnVH
import com.tokopedia.affiliate.ui.viewholder.AffiliateEducationSeeAllVH
import com.tokopedia.affiliate.ui.viewholder.AffiliateEducationSocialRVVH
import com.tokopedia.affiliate.ui.viewholder.AffiliateEducationSocialVH
import com.tokopedia.affiliate.ui.viewholder.AffiliateEducationTutorialRVVH
import com.tokopedia.affiliate.ui.viewholder.AffiliateEducationTutorialVH
import com.tokopedia.affiliate.ui.viewholder.AffiliateHeaderItemVH
import com.tokopedia.affiliate.ui.viewholder.AffiliateHomeUserDataVH
import com.tokopedia.affiliate.ui.viewholder.AffiliateHomeUserListDataVH
import com.tokopedia.affiliate.ui.viewholder.AffiliateNoPromoItemFoundVH
import com.tokopedia.affiliate.ui.viewholder.AffiliatePerformaSharedProductCardsItemVH
import com.tokopedia.affiliate.ui.viewholder.AffiliatePerformanceChipRVVH
import com.tokopedia.affiliate.ui.viewholder.AffiliatePerformanceChipVH
import com.tokopedia.affiliate.ui.viewholder.AffiliatePortfolioButtonItemVH
import com.tokopedia.affiliate.ui.viewholder.AffiliatePortfolioItemVH
import com.tokopedia.affiliate.ui.viewholder.AffiliateProductMetricVH
import com.tokopedia.affiliate.ui.viewholder.AffiliateProductShimmerCardItemVH
import com.tokopedia.affiliate.ui.viewholder.AffiliatePromotionCardItemVH
import com.tokopedia.affiliate.ui.viewholder.AffiliatePromotionErrorCardItemVH
import com.tokopedia.affiliate.ui.viewholder.AffiliatePromotionShopItemVH
import com.tokopedia.affiliate.ui.viewholder.AffiliateSSAShopItemVH
import com.tokopedia.affiliate.ui.viewholder.AffiliateShareItemViewHolder
import com.tokopedia.affiliate.ui.viewholder.AffiliateSharedProductCardsItemVH
import com.tokopedia.affiliate.ui.viewholder.AffiliateStaggeredPromotionCardItemVH
import com.tokopedia.affiliate.ui.viewholder.AffiliateStaggeredShimmerCardItemVH
import com.tokopedia.affiliate.ui.viewholder.AffiliateTermsAndConditionVH
import com.tokopedia.affiliate.ui.viewholder.AffiliateTrafficProductItemVH
import com.tokopedia.affiliate.ui.viewholder.AffiliateTransactionHistoryItemVH
import com.tokopedia.affiliate.ui.viewholder.AffiliateWithdrawalTitleItemVH
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

class AffiliateAdapterFactory(
    private var shareButtonInterface: ShareButtonInterface? = null,
    var productClickInterface: ProductClickInterface? = null,
    private var promotionClickInterface: PromotionClickInterface? = null,
    private var addSocialInterface: AddSocialInterface? = null,
    private var portfolioClickInterface: PortfolioClickInterface? = null,
    private var onFocusChangeInterface: PortfolioUrlTextUpdateInterface? = null,
    private var onDateRangeClickInterface: AffiliateDatePickerRangeChangeInterface? = null,
    private var onPerformaGridClick: AffiliatePerformaClickInterfaces? = null,
    private var bottomNavBarClickListener: AffiliateBottomNavBarInterface? = null,
    private var affiliateInfoClickInterfaces: AffiliateInfoClickInterfaces? = null,
    private var affiliatePerformanceChipClick: AffiliatePerformanceChipClick? = null,
    private var affiliateEducationEventArticleClickInterface: AffiliateEducationEventArticleClickInterface? = null,
    private var affiliateEducationLearnClickInterface: AffiliateEducationLearnClickInterface? = null,
    private var affiliateEducationTopicTutorialClickInterface: AffiliateEducationTopicTutorialClickInterface? = null,
    private var educationSocialCTAClickInterface: AffiliateEducationSocialCTAClickInterface? = null,
    private var educationSeeAllCardClickInterface: AffiliateEducationSeeAllCardClickInterface? = null,
    private var educationBannerClickInterface: AffiliateEducationBannerClickInterface? = null,
    private var affiliateEduCategoryChipClick: AffiliateEduCategoryChipClick? = null
) :
    BaseAdapterTypeFactory(), AffiliateAdapterTypeFactory {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            AffiliateSharedProductCardsItemVH.LAYOUT -> AffiliateSharedProductCardsItemVH(parent, productClickInterface)
            AffiliatePerformaSharedProductCardsItemVH.LAYOUT -> AffiliatePerformaSharedProductCardsItemVH(parent, productClickInterface)
            AffiliateDataCardShimmerItemVH.LAYOUT -> AffiliateDataCardShimmerItemVH(parent)
            AffiliateProductShimmerCardItemVH.LAYOUT -> AffiliateProductShimmerCardItemVH(parent)
            AffiliateShareItemViewHolder.LAYOUT -> AffiliateShareItemViewHolder(parent, shareButtonInterface, addSocialInterface)
            AffiliatePromotionCardItemVH.LAYOUT -> AffiliatePromotionCardItemVH(parent, promotionClickInterface)
            AffiliatePromotionShopItemVH.LAYOUT -> AffiliatePromotionShopItemVH(parent, promotionClickInterface)
            AffiliatePromotionErrorCardItemVH.LAYOUT -> AffiliatePromotionErrorCardItemVH(parent, promotionClickInterface)
            AffiliatePortfolioItemVH.LAYOUT -> AffiliatePortfolioItemVH(parent, onFocusChangeInterface)
            AffiliateHeaderItemVH.LAYOUT -> AffiliateHeaderItemVH(parent)
            AffiliatePortfolioButtonItemVH.LAYOUT -> AffiliatePortfolioButtonItemVH(parent, portfolioClickInterface)
            AffiliateTermsAndConditionVH.LAYOUT -> AffiliateTermsAndConditionVH(parent)
            AffiliateStaggeredShimmerCardItemVH.LAYOUT -> AffiliateStaggeredShimmerCardItemVH(parent)
            AffiliateStaggeredPromotionCardItemVH.LAYOUT -> AffiliateStaggeredPromotionCardItemVH(parent, promotionClickInterface)
            AffiliateTransactionHistoryItemVH.LAYOUT -> AffiliateTransactionHistoryItemVH(parent)
            AffiliateCommissionDetailsItemVH.LAYOUT -> AffiliateCommissionDetailsItemVH(parent, affiliateInfoClickInterfaces)
            AffiliateCommisionDivderItemVH.LAYOUT -> AffiliateCommisionDivderItemVH(parent)
            AffiliateHomeUserDataVH.LAYOUT -> AffiliateHomeUserDataVH(parent, onPerformaGridClick)
            AffiliateHomeUserListDataVH.LAYOUT -> AffiliateHomeUserListDataVH(parent, onPerformaGridClick)
            AffiliateDateFilterVH.LAYOUT -> AffiliateDateFilterVH(parent, onDateRangeClickInterface)
            AffiliateNoPromoItemFoundVH.LAYOUT -> AffiliateNoPromoItemFoundVH(parent, bottomNavBarClickListener)
            AffiliateCommisionThickDivderItemVH.LAYOUT -> AffiliateCommisionThickDivderItemVH(parent)
            AffiliateWithdrawalTitleItemVH.LAYOUT -> AffiliateWithdrawalTitleItemVH(parent)
            AffiliateTrafficProductItemVH.LAYOUT -> AffiliateTrafficProductItemVH(parent)
            AffiliateProductMetricVH.LAYOUT -> AffiliateProductMetricVH(parent)
            AffiliatePerformanceChipRVVH.LAYOUT -> AffiliatePerformanceChipRVVH(parent, affiliatePerformanceChipClick)
            AffiliatePerformanceChipVH.LAYOUT -> AffiliatePerformanceChipVH(parent, affiliatePerformanceChipClick)
            AffiliateEducationBannerItemVH.LAYOUT -> AffiliateEducationBannerItemVH(parent, educationBannerClickInterface)
            AffiliateEducationArticleTopicRVVH.LAYOUT -> AffiliateEducationArticleTopicRVVH(parent, affiliateEducationTopicTutorialClickInterface)
            AffiliateEducationArticleTopicVH.LAYOUT -> AffiliateEducationArticleTopicVH(parent, affiliateEducationTopicTutorialClickInterface)
            AffiliateEducationEventRVVH.LAYOUT -> AffiliateEducationEventRVVH(parent, affiliateEducationEventArticleClickInterface)
            AffiliateEducationEventVH.LAYOUT -> AffiliateEducationEventVH(parent, affiliateEducationEventArticleClickInterface)
            AffiliateEducationArticleRVVH.LAYOUT -> AffiliateEducationArticleRVVH(parent, affiliateEducationEventArticleClickInterface)
            AffiliateEducationArticleVH.LAYOUT -> AffiliateEducationArticleVH(parent, affiliateEducationEventArticleClickInterface)
            AffiliateEducationTutorialRVVH.LAYOUT -> AffiliateEducationTutorialRVVH(parent, affiliateEducationTopicTutorialClickInterface)
            AffiliateEducationTutorialVH.LAYOUT -> AffiliateEducationTutorialVH(parent, affiliateEducationTopicTutorialClickInterface)
            AffiliateEducationSocialRVVH.LAYOUT -> AffiliateEducationSocialRVVH(parent, educationSocialCTAClickInterface)
            AffiliateEducationSocialVH.LAYOUT -> AffiliateEducationSocialVH(parent, educationSocialCTAClickInterface)
            AffiliateEducationLearnVH.LAYOUT -> AffiliateEducationLearnVH(parent, affiliateEducationLearnClickInterface)
            AffiliateEducationSeeAllVH.LAYOUT -> AffiliateEducationSeeAllVH(parent, educationSeeAllCardClickInterface)
            AffiliateEduCategoryChipVH.LAYOUT -> AffiliateEduCategoryChipVH(parent, affiliateEduCategoryChipClick)
            AffiliateSSAShopItemVH.LAYOUT -> AffiliateSSAShopItemVH(parent, productClickInterface)
            else -> super.createViewHolder(parent, type)
        }
    }

    override fun type(viewModelShared: AffiliateSharedProductCardsModel): Int {
        return AffiliateSharedProductCardsItemVH.LAYOUT
    }

    override fun type(viewModelShared: AffiliatePerformaSharedProductCardsModel): Int {
        return AffiliatePerformaSharedProductCardsItemVH.LAYOUT
    }

    override fun type(viewModel: AffiliateShimmerModel): Int {
        return AffiliateProductShimmerCardItemVH.LAYOUT
    }

    override fun type(viewModel: AffiliateShareModel): Int {
        return AffiliateShareItemViewHolder.LAYOUT
    }

    override fun type(viewModel: AffiliatePromotionCardModel): Int {
        return AffiliatePromotionCardItemVH.LAYOUT
    }

    override fun type(viewModel: AffiliatePromotionErrorCardModel): Int {
        return AffiliatePromotionErrorCardItemVH.LAYOUT
    }

    override fun type(viewModel: AffiliatePortfolioUrlModel): Int {
        return AffiliatePortfolioItemVH.LAYOUT
    }

    override fun type(viewModel: AffiliateHeaderModel): Int {
        return AffiliateHeaderItemVH.LAYOUT
    }

    override fun type(viewModel: AffiliatePortfolioButtonModel): Int {
        return AffiliatePortfolioButtonItemVH.LAYOUT
    }

    override fun type(viewModel: AffiliateTermsAndConditionModel): Int {
        return AffiliateTermsAndConditionVH.LAYOUT
    }

    override fun type(viewModel: AffiliateUserPerformanceModel): Int {
        return AffiliateHomeUserDataVH.LAYOUT
    }

    override fun type(viewModel: AffiliateUserPerformanceListModel): Int {
        return AffiliateHomeUserListDataVH.LAYOUT
    }

    override fun type(viewModel: AffiliateDateFilterModel): Int {
        return AffiliateDateFilterVH.LAYOUT
    }

    override fun type(viewModel: AffiliateStaggeredShimmerModel): Int {
        return AffiliateStaggeredShimmerCardItemVH.LAYOUT
    }

    override fun type(viewModel: AffiliateStaggeredPromotionCardModel): Int {
        return AffiliateStaggeredPromotionCardItemVH.LAYOUT
    }

    override fun type(viewModel: AffiliateTransactionHistoryItemModel): Int {
        return AffiliateTransactionHistoryItemVH.LAYOUT
    }

    override fun type(viewModel: AffiliateCommissionItemModel): Int {
        return AffiliateCommissionDetailsItemVH.LAYOUT
    }

    override fun type(viewModel: AffiliateCommisionDividerItemModel): Int {
        return AffiliateCommisionDivderItemVH.LAYOUT
    }

    override fun type(viewModel: AffiliateCommisionThickDividerItemModel): Int {
        return AffiliateCommisionThickDivderItemVH.LAYOUT
    }

    override fun type(viewModel: AffiliateNoPromoItemFoundModel): Int {
        return AffiliateNoPromoItemFoundVH.LAYOUT
    }

    override fun type(viewModel: AffiliateDataPlatformShimmerModel): Int {
        return AffiliateDataCardShimmerItemVH.LAYOUT
    }

    override fun type(viewModel: AffiliateWithdrawalTitleItemModel): Int {
        return AffiliateWithdrawalTitleItemVH.LAYOUT
    }

    override fun type(viewModel: AffiliateTrafficCardModel): Int {
        return AffiliateTrafficProductItemVH.LAYOUT
    }

    override fun type(viewModel: AffiliateProductCardMetricsModel): Int {
        return AffiliateProductMetricVH.LAYOUT
    }

    override fun type(viewModel: AffiliatePromotionShopModel): Int {
        return AffiliatePromotionShopItemVH.LAYOUT
    }

    override fun type(viewModel: AffiliatePerformanceChipRVModel): Int {
        return AffiliatePerformanceChipRVVH.LAYOUT
    }

    override fun type(viewModel: AffiliatePerformanceChipModel): Int {
        return AffiliatePerformanceChipVH.LAYOUT
    }

    override fun type(viewModel: AffiliateEducationBannerUiModel): Int {
        return AffiliateEducationBannerItemVH.LAYOUT
    }

    override fun type(viewModel: AffiliateEducationArticleTopicRVUiModel): Int {
        return AffiliateEducationArticleTopicRVVH.LAYOUT
    }

    override fun type(viewModel: AffiliateEducationArticleTopicUiModel): Int {
        return AffiliateEducationArticleTopicVH.LAYOUT
    }

    override fun type(viewModel: AffiliateEducationEventRVUiModel): Int {
        return AffiliateEducationEventRVVH.LAYOUT
    }

    override fun type(viewModel: AffiliateEducationEventUiModel): Int {
        return AffiliateEducationEventVH.LAYOUT
    }

    override fun type(viewModel: AffiliateEducationArticleRVUiModel): Int {
        return AffiliateEducationArticleRVVH.LAYOUT
    }

    override fun type(viewModel: AffiliateEducationArticleUiModel): Int {
        return AffiliateEducationArticleVH.LAYOUT
    }

    override fun type(viewModel: AffiliateEducationTutorialRVUiModel): Int {
        return AffiliateEducationTutorialRVVH.LAYOUT
    }

    override fun type(viewModel: AffiliateEducationTutorialUiModel): Int {
        return AffiliateEducationTutorialVH.LAYOUT
    }

    override fun type(viewModel: AffiliateEducationSocialRVUiModel): Int {
        return AffiliateEducationSocialRVVH.LAYOUT
    }

    override fun type(viewModel: AffiliateEducationSocialUiModel): Int {
        return AffiliateEducationSocialVH.LAYOUT
    }

    override fun type(viewModel: AffiliateEducationLearnUiModel): Int {
        return AffiliateEducationLearnVH.LAYOUT
    }

    override fun type(viewModel: AffiliateEducationSeeAllUiModel): Int {
        return AffiliateEducationSeeAllVH.LAYOUT
    }

    override fun type(viewModel: AffiliateEduCategoryChipModel): Int =
        AffiliateEduCategoryChipVH.LAYOUT

    override fun type(viewModel: AffiliateSSAShopUiModel): Int =
        AffiliateSSAShopItemVH.LAYOUT
}
