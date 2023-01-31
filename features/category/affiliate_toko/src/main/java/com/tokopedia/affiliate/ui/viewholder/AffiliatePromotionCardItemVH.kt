package com.tokopedia.affiliate.ui.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.ALMOST_OOS
import com.tokopedia.affiliate.AVAILABLE
import com.tokopedia.affiliate.AffiliateAnalytics
import com.tokopedia.affiliate.EMPTY_STOCK
import com.tokopedia.affiliate.PRODUCT_INACTIVE
import com.tokopedia.affiliate.SHOP_INACTIVE
import com.tokopedia.affiliate.interfaces.PromotionClickInterface
import com.tokopedia.affiliate.model.pojo.AffiliatePromotionBottomSheetParams
import com.tokopedia.affiliate.model.response.AffiliateSearchData
import com.tokopedia.affiliate.ui.custom.AffiliatePromotionProductCard
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliatePromotionCardModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.productcard.ProductCardListView
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.user.session.UserSession

class AffiliatePromotionCardItemVH(
    itemView: View,
    private val promotionClickInterface: PromotionClickInterface?
) :
    AbstractViewHolder<AffiliatePromotionCardModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_promotion_product_card_item_layout
    }

    override fun bind(element: AffiliatePromotionCardModel?) {
        element?.promotionItem?.let {
            itemView.findViewById<ProductCardListView>(R.id.affiliate_product_card).setProductModel(
                AffiliatePromotionProductCard.toAffiliateProductModel(it)
            )
        }

        itemView.findViewById<UnifyButton>(com.tokopedia.productcard.R.id.buttonNotify)?.run {
            visibility = View.VISIBLE
            buttonType = UnifyButton.Type.MAIN
            buttonVariant = UnifyButton.Variant.GHOST
            text = context.getString(R.string.affiliate_promo)
            var commission = ""
            element?.promotionItem?.commission?.amount?.let {
                commission = it.toString()
            }
            setOnClickListener {
                sendClickEvent(element?.promotionItem)
                promotionClickInterface?.onPromotionClick(
                    element?.promotionItem?.itemId ?: "",
                    element?.promotionItem?.title ?: "",
                    element?.promotionItem?.image?.androidURL ?: "",
                    element?.promotionItem?.cardUrl ?: "",
                    bindingAdapterPosition,
                    commission,
                    getStatus(element?.promotionItem),
                    element?.promotionItem?.type,
                    ssaInfo = AffiliatePromotionBottomSheetParams.SSAInfo(
                        element?.promotionItem?.ssaStatus.orFalse(),
                        element?.promotionItem?.ssaMessage.orEmpty(),
                        element?.promotionItem?.message.orEmpty(),
                        AffiliatePromotionBottomSheetParams.SSAInfo.Label(
                            labelText = element?.promotionItem?.label?.labelText.orEmpty(),
                            labelType = element?.promotionItem?.label?.labelType.orEmpty()
                        )
                    )
                )
            }
            if (element?.promotionItem?.status?.isLinkGenerationAllowed == false) {
                buttonType = UnifyButton.Type.ALTERNATE
                setOnClickListener(null)
            }
        }
    }

    private fun sendClickEvent(item: AffiliateSearchData.SearchAffiliate.Data.Card.Item?) {
        AffiliateAnalytics.trackEventImpression(
            AffiliateAnalytics.EventKeys.SELECT_CONTENT,
            AffiliateAnalytics.ActionKeys.CLICK_PROMOSIKAN_SEARCH_RESULT_PAGE,
            AffiliateAnalytics.CategoryKeys.AFFILIATE_PROMOSIKAN_PAGE,
            UserSession(itemView.context).userId,
            item?.productID,
            bindingAdapterPosition + 1,
            item?.title,
            "${item?.productID} - ${item?.commission?.amount} - ${getStatus(item)}",
            AffiliateAnalytics.ItemKeys.AFFILIATE_SEARCH_PROMOSIKAN_CLICK
        )
    }

    private fun getStatus(item: AffiliateSearchData.SearchAffiliate.Data.Card.Item?): String {
        var status = ""
        if (item?.status?.messages?.isNotEmpty() == true) {
            when (item.status?.messages?.first()?.messageType) {
                AVAILABLE -> status = AffiliateAnalytics.LabelKeys.AVAILABLE
                ALMOST_OOS -> status = AffiliateAnalytics.LabelKeys.ALMOST_OOS
                EMPTY_STOCK -> status = AffiliateAnalytics.LabelKeys.EMPTY_STOCK
                PRODUCT_INACTIVE -> status = AffiliateAnalytics.LabelKeys.PRODUCT_INACTIVE
                SHOP_INACTIVE -> status = AffiliateAnalytics.LabelKeys.SHOP_INACTIVE
            }
        }
        return status
    }
}
