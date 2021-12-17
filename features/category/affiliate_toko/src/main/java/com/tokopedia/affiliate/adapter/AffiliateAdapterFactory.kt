package com.tokopedia.affiliate.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.interfaces.*
import com.tokopedia.affiliate.ui.custom.AffiliateBottomNavBarInterface
import com.tokopedia.affiliate.ui.viewholder.*
import com.tokopedia.affiliate.ui.viewholder.viewmodel.*

class AffiliateAdapterFactory(
        private var shareButtonInterface: ShareButtonInterface? = null,
        var productClickInterface : ProductClickInterface? = null,
        private var promotionClickInterface : PromotionClickInterface? = null,
        private var addSocialInterface  : AddSocialInterface? = null,
        private var portfolioClickInterface: PortfolioClickInterface? = null,
        private var onFocusChangeInterface: PortfolioUrlTextUpdateInterface? = null,
        private var onDateRangeClickInterface: AffiliateDatePickerRangeChangeInterface? = null,
        private var onPerformaGridClick: AffiliatePerformaClickInterfaces? = null,
        private var bottomNavBarClickListener : AffiliateBottomNavBarInterface? = null,
        private var affiliateInfoClickInterfaces: AffiliateInfoClickInterfaces? = null
)
    : BaseAdapterTypeFactory(), AffiliateAdapterTypeFactory {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            AffiliateSharedProductCardsItemVH.LAYOUT -> AffiliateSharedProductCardsItemVH(parent, productClickInterface)
            AffiliatePerformaSharedProductCardsItemVH.LAYOUT -> AffiliatePerformaSharedProductCardsItemVH(parent, productClickInterface)
            AffiliateDataCardShimmerItemVH.LAYOUT -> AffiliateDataCardShimmerItemVH(parent)
            AffiliateProductShimmerCardItemVH.LAYOUT -> AffiliateProductShimmerCardItemVH(parent)
            AffiliateShareItemViewHolder.LAYOUT -> AffiliateShareItemViewHolder(parent, shareButtonInterface,addSocialInterface)
            AffiliatePromotionCardItemVH.LAYOUT -> AffiliatePromotionCardItemVH(parent,promotionClickInterface)
            AffiliatePromotionErrorCardItemVH.LAYOUT -> AffiliatePromotionErrorCardItemVH(parent,promotionClickInterface)
            AffiliatePortfolioItemVH.LAYOUT -> AffiliatePortfolioItemVH(parent,onFocusChangeInterface)
            AffiliateHeaderItemVH.LAYOUT -> AffiliateHeaderItemVH(parent)
            AffiliatePortfolioButtonItemVH.LAYOUT -> AffiliatePortfolioButtonItemVH(parent,portfolioClickInterface)
            AffiliateTermsAndConditionVH.LAYOUT -> AffiliateTermsAndConditionVH(parent)
            AffiliateStaggeredShimmerCardItemVH.LAYOUT -> AffiliateStaggeredShimmerCardItemVH(parent)
            AffiliateStaggeredPromotionCardItemVH.LAYOUT -> AffiliateStaggeredPromotionCardItemVH(parent,promotionClickInterface)
            AffiliateTransactionHistoryItemVH.LAYOUT -> AffiliateTransactionHistoryItemVH(parent)
            AffiliateCommissionDetailsItemVH.LAYOUT -> AffiliateCommissionDetailsItemVH(parent,affiliateInfoClickInterfaces)
            AffiliateCommisionDivderItemVH.LAYOUT -> AffiliateCommisionDivderItemVH(parent)
            AffiliateHomeUserDataVH.LAYOUT -> AffiliateHomeUserDataVH(parent, onPerformaGridClick)
            AffiliateHomeUserListDataVH.LAYOUT -> AffiliateHomeUserListDataVH(parent, onPerformaGridClick)
            AffiliateDateFilterVH.LAYOUT -> AffiliateDateFilterVH(parent,onDateRangeClickInterface)
            AffiliateNoPromoItemFoundVH.LAYOUT -> AffiliateNoPromoItemFoundVH(parent,bottomNavBarClickListener)
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
        return  AffiliateTermsAndConditionVH.LAYOUT
    }

    override fun type(viewModel: AffiliateUserPerformanceModel): Int {
       return  AffiliateHomeUserDataVH.LAYOUT
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

    override fun type(viewModel: AffiliateNoPromoItemFoundModel): Int {
      return AffiliateNoPromoItemFoundVH.LAYOUT
    }

    override fun type(viewModel: AffiliateDataPlatformShimmerModel): Int {
        return AffiliateDataCardShimmerItemVH.LAYOUT
    }
}
