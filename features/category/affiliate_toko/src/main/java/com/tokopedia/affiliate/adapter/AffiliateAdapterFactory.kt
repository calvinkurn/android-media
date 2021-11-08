package com.tokopedia.affiliate.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.interfaces.PortfolioUrlTextUpdateInterface
import com.tokopedia.affiliate.interfaces.ProductClickInterface
import com.tokopedia.affiliate.interfaces.PromotionClickInterface
import com.tokopedia.affiliate.interfaces.ShareButtonInterface
import com.tokopedia.affiliate.ui.viewholder.*
import com.tokopedia.affiliate.ui.viewholder.viewmodel.*

class AffiliateAdapterFactory(
        private var shareButtonInterface: ShareButtonInterface? = null,
        var productClickInterface : ProductClickInterface? = null,
        private var promotionClickInterface : PromotionClickInterface? = null,
        private var onFoucusChangeInterface: PortfolioUrlTextUpdateInterface?=null)
    : BaseAdapterTypeFactory(), AffiliateAdapterTypeFactory {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            AffiliateSharedProductCardsItemVH.LAYOUT -> AffiliateSharedProductCardsItemVH(parent, productClickInterface)
            AffiliateProductShimmerCardItemVH.LAYOUT -> AffiliateProductShimmerCardItemVH(parent)
            AffiliateShareItemViewHolder.LAYOUT -> AffiliateShareItemViewHolder(parent, shareButtonInterface)
            AffiliatePromotionCardItemVH.LAYOUT -> AffiliatePromotionCardItemVH(parent,promotionClickInterface)
            AffiliatePromotionErrorCardItemVH.LAYOUT -> AffiliatePromotionErrorCardItemVH(parent,promotionClickInterface)
            AffiliatePortfolioItemVH.LAYOUT -> AffiliatePortfolioItemVH(parent,onFoucusChangeInterface)
            AffiliateHeaderItemVH.LAYOUT -> AffiliateHeaderItemVH(parent)
            AffiliatePortfolioButtonItemVH.LAYOUT -> AffiliatePortfolioButtonItemVH(parent)
            AffiliateTermsAndConditionVH.LAYOUT -> AffiliateTermsAndConditionVH(parent)
            AffiliateStaggeredShimmerCardItemVH.LAYOUT -> AffiliateStaggeredShimmerCardItemVH(parent)
            AffiliateStaggeredPromotionCardItemVH.LAYOUT -> AffiliateStaggeredPromotionCardItemVH(parent,promotionClickInterface)
            else -> super.createViewHolder(parent, type)
        }
    }

    override fun type(viewModelShared: AffiliateSharedProductCardsModel): Int {
        return AffiliateSharedProductCardsItemVH.LAYOUT
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

    override fun type(viewModel: AffiliateStaggeredShimmerModel): Int {
        return AffiliateStaggeredShimmerCardItemVH.LAYOUT
    }

    override fun type(viewModel: AffiliateStaggeredPromotionCardModel): Int {
        return AffiliateStaggeredPromotionCardItemVH.LAYOUT
    }
}
