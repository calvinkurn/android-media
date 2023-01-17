package com.tokopedia.affiliate.ui.viewholder

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.AVAILABLE
import com.tokopedia.affiliate.AffiliateAnalytics
import com.tokopedia.affiliate.SHOP_CLOSED
import com.tokopedia.affiliate.SHOP_INACTIVE
import com.tokopedia.affiliate.interfaces.PromotionClickInterface
import com.tokopedia.affiliate.model.response.AffiliateSearchData
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliatePromotionShopModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.loadImageRounded
import com.tokopedia.unifycomponents.DividerUnify
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSession
import java.util.Locale

class AffiliatePromotionShopItemVH(
    itemView: View,
    private val promotionClickInterface: PromotionClickInterface?
) : AbstractViewHolder<AffiliatePromotionShopModel>(itemView) {


    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_promotion_shop_card_item_layout

        private const val COMMISSION_AMOUNT_TYPE = 1
        private const val DISCOUNT_PERCENTAGE_TYPE = 2
        private const val PER_GOOD_SOLD = 6
        private const val ITEM_SOLD = 3
        private const val LOCATION = 4
    }

    override fun bind(element: AffiliatePromotionShopModel?) {
        element?.promotionItem?.let {
            itemView.findViewById<ImageView>(R.id.imageMain).loadImageRounded(it.image?.androidURL)
            itemView.findViewById<Typography>(R.id.textViewTitle).text = it.title
            if (!it.titleEmblem.isNullOrBlank()) {
                itemView.findViewById<ImageView>(R.id.imageTitleEmblem).apply {
                    visible()
                    loadImage(it.titleEmblem)
                }
            }
            getMessageData(it)?.let { message ->
                itemView.findViewById<Label>(R.id.labelProductStatus).apply {
                    if (message.isNotEmpty()) {
                        visible()
                        text = message
                    }
                }
            }
            setUpAdditionData(it)
            setUpFooterData(it)
            setUpPromotionClickListener(it)
        }
    }

    private fun setUpAdditionData(item: AffiliateSearchData.SearchAffiliate.Data.Card.Item) {
        getAdditionalDataFromType(item, COMMISSION_AMOUNT_TYPE)?.let { info ->
            itemView.findViewById<Typography>(R.id.textViewAdditionalInfo1).apply {
                visible()
                text = info.htmlText
                if (info.color?.isNotEmpty() == true) setTextColor(Color.parseColor(info.color))
            }
        }
        getAdditionalDataFromType(item, PER_GOOD_SOLD)?.let { info ->
            itemView.findViewById<Typography>(R.id.textViewAdditionalInfo2).apply {
                visible()
                text = info.htmlText
                if (info.color?.isNotEmpty() == true) setTextColor(Color.parseColor(info.color))
            }
        }
        getAdditionalDataFromType(
            item,
            DISCOUNT_PERCENTAGE_TYPE
        )?.let { info ->
            itemView.findViewById<Typography>(R.id.textViewAdditionalInfo2).apply {
                visible()
                text = String.format(
                    Locale.getDefault(),
                    "/%s",
                    info
                )
            }
        }
    }

    private fun setUpFooterData(item: AffiliateSearchData.SearchAffiliate.Data.Card.Item) {
        item.rating.let { rating ->
            if (rating.isMoreThanZero()) {
                itemView.findViewById<Typography>(R.id.textViewRating).apply {
                    visible()
                    text = item.rating.toString()
                }
                itemView.findViewById<ImageView>(R.id.imageRating).visible()
            }
        }
        getFooterDataFromType(item, LOCATION)?.let { footer ->
            itemView.findViewById<Typography>(R.id.textViewFooterLocation).apply {
                itemView.findViewById<ImageView>(R.id.imageFooter).visible()
                visible()
                text = footer.footerText
            }
        }
        getFooterDataFromType(item, ITEM_SOLD)?.let { footer ->
            itemView.findViewById<Typography>(R.id.textViewItemSold).apply {
                visible()
                text = footer.footerText
                if (item.rating?.isMoreThanZero() == true && footer.footerText?.isNotEmpty() == true)
                    itemView.findViewById<DividerUnify>(R.id.ratingDivider).visible()
            }
        }
    }

    private fun setUpPromotionClickListener(promotionItem: AffiliateSearchData.SearchAffiliate.Data.Card.Item) {
        itemView.findViewById<UnifyButton>(R.id.buttonPromotion)?.run {
            visibility = View.VISIBLE
            buttonType = UnifyButton.Type.MAIN
            buttonVariant = UnifyButton.Variant.GHOST
            text = context.getString(R.string.affiliate_promo)
            var commission = ""
            promotionItem.commission?.amount?.let {
                commission = it.toString()
            }
            setOnClickListener {
                sendClickEvent(promotionItem)
                promotionClickInterface?.onPromotionClick(
                    promotionItem.itemId,
                    promotionItem.title ?: "",
                    promotionItem.image?.androidURL ?: "",
                    promotionItem.cardUrl ?: "",
                    bindingAdapterPosition, commission,
                    getStatus(promotionItem),
                    promotionItem.type
                )
            }
            if (promotionItem.status?.isLinkGenerationAllowed == false) {
                buttonType = UnifyButton.Type.ALTERNATE
                setOnClickListener(null)
            }
        }
    }

    private fun sendClickEvent(item: AffiliateSearchData.SearchAffiliate.Data.Card.Item?) {
        AffiliateAnalytics.trackEventImpression(
            AffiliateAnalytics.EventKeys.SELECT_CONTENT,
            AffiliateAnalytics.ActionKeys.CLICK_SHOP_SEARCH_RESULT_PAGE,
            AffiliateAnalytics.CategoryKeys.AFFILIATE_PROMOSIKAN_PAGE,
            UserSession(itemView.context).userId,
            item?.itemId,
            bindingAdapterPosition + 1,
            item?.title,
            "${item?.itemId} - ${item?.commission?.amount} - ${getStatus(item)}",
            AffiliateAnalytics.ItemKeys.AFFILIATE_SEARCH_SHOP_CLICK
        )
    }

    private fun getStatus(item: AffiliateSearchData.SearchAffiliate.Data.Card.Item?): String {
        var status = ""
        if (item?.status?.messages?.isNotEmpty() == true) {
            when (item.status?.messages?.first()?.messageType) {
                AVAILABLE -> status = AffiliateAnalytics.LabelKeys.SHOP_ACTIVE
                SHOP_INACTIVE -> status = AffiliateAnalytics.LabelKeys.SHOP_INACTIVE
                SHOP_CLOSED -> status = AffiliateAnalytics.LabelKeys.SHOP_CLOSED
            }
        }
        return status
    }

    private fun getAdditionalDataFromType(
        item: AffiliateSearchData.SearchAffiliate.Data.Card.Item,
        type: Int
    ): AffiliateSearchData.SearchAffiliate.Data.Card.Item.AdditionalInformation? {
        return (item.additionalInformation?.find { it?.type == type })
    }

    private fun getFooterDataFromType(
        item: AffiliateSearchData.SearchAffiliate.Data.Card.Item,
        type: Int
    ): AffiliateSearchData.SearchAffiliate.Data.Card.Item.Footer? {
        return (item.footer?.find { it?.footerType == type })
    }

    private fun getMessageData(
        item: AffiliateSearchData.SearchAffiliate.Data.Card.Item,
    ): String? {
        return item.status?.messages?.firstOrNull()?.title
    }
}
