package com.tokopedia.affiliate.ui.viewholder

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.*
import com.tokopedia.affiliate.interfaces.PromotionClickInterface
import com.tokopedia.affiliate.model.response.AffiliateSearchData
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliatePromotionShopModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.loadImageRounded
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSession

class AffiliatePromotionShopItemVH(
    itemView: View,
    private val promotionClickInterface: PromotionClickInterface?
) : AbstractViewHolder<AffiliatePromotionShopModel>(itemView) {
    private enum class AdditionalInfoType(val type: Int) {
        COMMISSION_AMOUNT_TYPE(1),
        DISCOUNT_PERCENTAGE_TYPE(2),
        PER_GOOD_SOLD(6),
    }

    private enum class FooterType(val type: Int) {
        RATING(2),
        ITEM_SOLD(3),
        LOCATION(4)
    }

    private enum class MessageType(val type: Int) {
        OVERLAY_IMAGE_TYPE(1)
    }

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_promotion_shop_card_item_layout

    }

    override fun bind(element: AffiliatePromotionShopModel?) {
        element?.promotionItem?.let {
            itemView.findViewById<ImageView>(R.id.imageMain).loadImageRounded(it.image?.androidURL)
            itemView.findViewById<ImageView>(R.id.imageTitleEmblem).loadImage(it.titleEmblem)
            itemView.findViewById<Typography>(R.id.textViewTitle).text = it.title
            itemView.findViewById<Typography>(R.id.textViewRating).text = it.rating.toString()
            getAdditionalDataFromType(it, AdditionalInfoType.COMMISSION_AMOUNT_TYPE)?.let { info ->
                itemView.findViewById<Typography>(R.id.textViewAdditionalInfo1).apply {
                    visible()
                    text = info.htmlText
                    if(info.color?.isNotEmpty() == true) setTextColor(Color.parseColor(info.color))
                }
            }
            getAdditionalDataFromType(it, AdditionalInfoType.PER_GOOD_SOLD)?.let { info ->
                itemView.findViewById<Typography>(R.id.textViewAdditionalInfo2).apply {
                    visible()
                    text = info.htmlText
                    if(info.color?.isNotEmpty() == true) setTextColor(Color.parseColor(info.color))
                }
            }
            getAdditionalDataFromType(
                it,
                AdditionalInfoType.DISCOUNT_PERCENTAGE_TYPE
            )?.let { info ->
                itemView.findViewById<Typography>(R.id.textViewAdditionalInfo2).apply {
                    visible()
                    text = String.format(
                        "/%s",
                        info
                    )
                }
            }

            getFooterDataFromType(it, FooterType.LOCATION)?.let { footer ->
                itemView.findViewById<Typography>(R.id.textViewFooterLocation).apply {
                    visible()
                    text = footer.footerText
                }
            }
            getFooterDataFromType(it, FooterType.ITEM_SOLD)?.let { footer ->
                itemView.findViewById<Typography>(R.id.textViewItemSold).apply {
                    visible()
                    itemView.findViewById<Typography>(R.id.ratingDivider).visible()
                    text = footer.footerText
                }
            }
            getMessageDataFromType(it, MessageType.OVERLAY_IMAGE_TYPE)?.let { message ->
                itemView.findViewById<Label>(R.id.labelProductStatus).apply {
                    if(message.isNotEmpty()){
                        visible()
                        text = message
                    }
                }
            }
        }

        itemView.findViewById<UnifyButton>(R.id.buttonPromotion)?.run {
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
                    element?.promotionItem?.productID ?: "",
                    element?.promotionItem?.title ?: "",
                    element?.promotionItem?.image?.androidURL ?: "",
                    element?.promotionItem?.cardUrl ?: "",
                    adapterPosition, commission,
                    getStatus(element?.promotionItem),
                    element?.promotionItem?.type
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
            adapterPosition + 1,
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

    private fun getAdditionalDataFromType(
        item: AffiliateSearchData.SearchAffiliate.Data.Card.Item,
        type: AdditionalInfoType
    ): AffiliateSearchData.SearchAffiliate.Data.Card.Item.AdditionalInformation? {
        return (item.additionalInformation?.find { it?.type == type.type })
    }

    private fun getFooterDataFromType(
        item: AffiliateSearchData.SearchAffiliate.Data.Card.Item,
        type: FooterType
    ): AffiliateSearchData.SearchAffiliate.Data.Card.Item.Footer? {
        return (item.footer?.find { it?.footerType == type.type })
    }

    private fun getMessageDataFromType(
        item: AffiliateSearchData.SearchAffiliate.Data.Card.Item,
        type: MessageType
    ): String? {
        return (item.status?.messages?.find { it?.messageType == type.type })?.title
    }
}
