package com.tokopedia.affiliate.ui.viewholder

import android.graphics.Color
import android.os.Build
import android.text.Html
import android.view.View
import android.widget.ImageView
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.AVAILABLE
import com.tokopedia.affiliate.AffiliateAnalytics
import com.tokopedia.affiliate.SHOP_CLOSED
import com.tokopedia.affiliate.SHOP_INACTIVE
import com.tokopedia.affiliate.interfaces.PromotionClickInterface
import com.tokopedia.affiliate.model.pojo.AffiliatePromotionBottomSheetParams
import com.tokopedia.affiliate.model.response.AffiliateSearchData
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliatePromotionShopModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.loadImageRounded
import com.tokopedia.unifycomponents.DividerUnify
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.Label.Companion.HIGHLIGHT_LIGHT_GREEN
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

    private val ssaLabel = itemView.findViewById<Label>(R.id.ssa_label)
    private val imageMain = itemView.findViewById<ImageView>(R.id.imageMain)
    private val textViewTitle = itemView.findViewById<Typography>(R.id.textViewTitle)
    private val imageTitleEmblem = itemView.findViewById<ImageView>(R.id.imageTitleEmblem)
    private val labelProductStatus = itemView.findViewById<Label>(R.id.labelProductStatus)
    private val textViewAdditionalInfo1 =
        itemView.findViewById<Typography>(R.id.textViewAdditionalInfo1)
    private val textViewAdditionalInfo2 =
        itemView.findViewById<Typography>(R.id.textViewAdditionalInfo2)
    private val textViewRating = itemView.findViewById<Typography>(R.id.textViewRating)
    private val imageRating = itemView.findViewById<ImageView>(R.id.imageRating)
    private val textViewFooterLocation =
        itemView.findViewById<Typography>(R.id.textViewFooterLocation)
    private val imageFooter = itemView.findViewById<ImageView>(R.id.imageFooter)
    private val textViewItemSold = itemView.findViewById<Typography>(R.id.textViewItemSold)
    private val ratingDivider = itemView.findViewById<DividerUnify>(R.id.ratingDivider)
    private val buttonPromotion = itemView.findViewById<UnifyButton>(R.id.buttonPromotion)

    override fun bind(element: AffiliatePromotionShopModel?) {
        element?.promotionItem?.let {
            imageMain.loadImageRounded(it.image?.androidURL)
            textViewTitle.text = it.title
            if (!it.titleEmblem.isNullOrBlank()) {
                imageTitleEmblem.apply {
                    visible()
                    loadImage(it.titleEmblem)
                }
            }
            ssaLabel.apply {
                isVisible = it.ssaStatus.orFalse()
                text = it.label?.labelText
                setLabelType(HIGHLIGHT_LIGHT_GREEN)
            }
            getMessageData(it)?.let { message ->
                labelProductStatus.apply {
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
        if (item.ssaStatus == true) {
            textViewAdditionalInfo1.apply {
                visible()
                text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Html.fromHtml(item.message, Html.FROM_HTML_MODE_LEGACY)
                } else {
                    Html.fromHtml(item.message)
                }
            }
        } else {
            getAdditionalDataFromType(item, COMMISSION_AMOUNT_TYPE)?.let { info ->
                textViewAdditionalInfo1.apply {
                    visible()
                    text = info.htmlText
                    if (info.color?.isNotEmpty() == true) setTextColor(Color.parseColor(info.color))
                }
            }
        }
        getAdditionalDataFromType(item, PER_GOOD_SOLD)?.let { info ->
            textViewAdditionalInfo2.apply {
                isVisible = item.ssaStatus == false
                text = info.htmlText
                if (info.color?.isNotEmpty() == true) setTextColor(Color.parseColor(info.color))
            }
        }
        getAdditionalDataFromType(
            item,
            DISCOUNT_PERCENTAGE_TYPE
        )?.let { info ->
            textViewAdditionalInfo2.apply {
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
                textViewRating.apply {
                    visible()
                    text = item.rating.toString()
                }
                imageRating.visible()
            }
        }
        getFooterDataFromType(item, LOCATION)?.let { footer ->
            textViewFooterLocation.apply {
                imageFooter.visible()
                visible()
                text = footer.footerText
            }
        }
        getFooterDataFromType(item, ITEM_SOLD)?.let { footer ->
            textViewItemSold.apply {
                visible()
                text = footer.footerText
                if (item.rating?.isMoreThanZero() == true && footer.footerText?.isNotEmpty() == true) {
                    ratingDivider.visible()
                }
            }
        }
    }

    private fun setUpPromotionClickListener(promotionItem: AffiliateSearchData.SearchAffiliate.Data.Card.Item) {
        buttonPromotion?.run {
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
                    bindingAdapterPosition,
                    commission,
                    getStatus(promotionItem),
                    promotionItem.type,
                    ssaInfo = AffiliatePromotionBottomSheetParams.SSAInfo(
                        promotionItem.ssaStatus.orFalse(),
                        promotionItem.ssaMessage.orEmpty(),
                        promotionItem.message.orEmpty(),
                        AffiliatePromotionBottomSheetParams.SSAInfo.Label(
                            labelText = promotionItem.label?.labelText.orEmpty(),
                            labelType = promotionItem.label?.labelType.orEmpty()
                        )
                    )
                )
            }
            if (promotionItem.status?.isLinkGenerationAllowed == false) {
                buttonType = UnifyButton.Type.ALTERNATE
                setOnClickListener(null)
            }
        }
    }

    private fun sendClickEvent(item: AffiliateSearchData.SearchAffiliate.Data.Card.Item?) {
        var label = getStatus(item)
        if (item?.ssaStatus == true) {
            label += " - komisi  extra"
        }
        AffiliateAnalytics.trackEventImpression(
            AffiliateAnalytics.EventKeys.SELECT_CONTENT,
            AffiliateAnalytics.ActionKeys.CLICK_SHOP_SEARCH_RESULT_PAGE,
            AffiliateAnalytics.CategoryKeys.AFFILIATE_PROMOSIKAN_PAGE,
            UserSession(itemView.context).userId,
            item?.itemId,
            bindingAdapterPosition + 1,
            item?.title,
            "${item?.itemId} - ${item?.commission?.amount} - $label",
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
        item: AffiliateSearchData.SearchAffiliate.Data.Card.Item
    ): String? {
        return item.status?.messages?.firstOrNull()?.title
    }
}
