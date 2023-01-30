package com.tokopedia.affiliate.ui.viewholder

import android.os.Build
import android.text.Html
import android.view.View
import android.widget.ImageView
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.AVAILABLE
import com.tokopedia.affiliate.AffiliateAnalytics
import com.tokopedia.affiliate.PAGE_TYPE_SHOP
import com.tokopedia.affiliate.interfaces.ProductClickInterface
import com.tokopedia.affiliate.model.pojo.AffiliatePromotionBottomSheetParams
import com.tokopedia.affiliate.model.response.AffiliateSSAShopListResponse
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateSSAShopUiModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.loadImageRounded
import com.tokopedia.unifycomponents.DividerUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSession

class AffiliateSSAShopItemVH(
    itemView: View,
    private val productClickInterface: ProductClickInterface?
) : AbstractViewHolder<AffiliateSSAShopUiModel>(itemView) {
    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_ssa_shop_item_layout
    }

    private val shopImage = itemView.findViewById<ImageView>(R.id.imageMain)
    private val shopName = itemView.findViewById<Typography>(R.id.textViewTitle)
    private val imageBadge = itemView.findViewById<ImageView>(R.id.imageTitleEmblem)
    private val ssaLabel = itemView.findViewById<Label>(R.id.ssa_label)
    private val ssaMessage = itemView.findViewById<Typography>(R.id.ssa_message)
    private val textRating = itemView.findViewById<Typography>(R.id.textViewRating)
    private val imageRating = itemView.findViewById<ImageView>(R.id.imageRating)
    private val textLocation = itemView.findViewById<Typography>(R.id.textViewFooterLocation)
    private val imageLocation = itemView.findViewById<ImageUnify>(R.id.imageFooter)
    private val itemSold = itemView.findViewById<Typography>(R.id.textViewItemSold)
    private val buttonPromotion = itemView.findViewById<UnifyButton>(R.id.buttonPromotion)
    private val ratingDivider = itemView.findViewById<DividerUnify>(R.id.ratingDivider)

    override fun bind(element: AffiliateSSAShopUiModel?) {
        element?.ssaShop?.let {
            shopImage.loadImageRounded(it.ssaShopDetail?.imageURL?.androidURL)
            shopName.text = it.ssaShopDetail?.shopName
            if (it.ssaShopDetail?.badgeURL?.isNotEmpty() == true) {
                imageBadge.apply {
                    visible()
                    loadImage(it.ssaShopDetail.badgeURL)
                }
            }
            ssaMessage.apply {
                visible()
                text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Html.fromHtml(it.ssaShopDetail?.message, Html.FROM_HTML_MODE_LEGACY)
                } else {
                    Html.fromHtml(it.ssaShopDetail?.message)
                }
            }
            ssaLabel.apply {
                isVisible = it.ssaShopDetail?.ssaStatus == true
                text = it.ssaShopDetail?.label?.labelText
            }
            setUpFooterData(it.ssaShopDetail)
            setUpPromotionClickListener(it)
        }
    }

    private fun setUpFooterData(item: AffiliateSSAShopListResponse.Data.SSAShop.ShopDataItem.SSAShopDetail?) {
        item?.rating?.let { rating ->
            if (rating > 0) {
                textRating.apply {
                    visible()
                    text = item.rating.toString()
                }
                imageRating.visible()
            }
        }
        textLocation.apply {
            imageLocation.visible()
            visible()
            text = item?.shopLocation
        }
        itemSold.apply {
            visible()
            text = getString(R.string.affiliate_ssa_terjual, item?.quantitySold.toString())
            ratingDivider.isVisible =
                item?.rating != null && item.rating > 0 && item.quantitySold != null
        }
    }

    private fun setUpPromotionClickListener(item: AffiliateSSAShopListResponse.Data.SSAShop.ShopDataItem?) {
        buttonPromotion?.run {
            visibility = View.VISIBLE
            buttonType = UnifyButton.Type.MAIN
            buttonVariant = UnifyButton.Variant.GHOST
            text = context.getString(R.string.affiliate_promo)
            setOnClickListener {
                sendClickEvent(item)
                productClickInterface?.onProductClick(
                    item?.ssaShopDetail?.shopId.toString(),
                    item?.ssaShopDetail?.shopName.orEmpty(),
                    item?.ssaShopDetail?.imageURL?.androidURL.orEmpty(),
                    item?.ssaShopDetail?.uRLDetail?.androidURL.orEmpty(),
                    item?.ssaShopDetail?.shopId.toString(),
                    AVAILABLE,
                    PAGE_TYPE_SHOP,
                    ssaInfo = AffiliatePromotionBottomSheetParams.SSAInfo(
                        ssaStatus = item?.ssaShopDetail?.ssaStatus.orFalse(),
                        ssaMessage = item?.ssaShopDetail?.ssaMessage.orEmpty(),
                        message = item?.ssaShopDetail?.message.orEmpty(),
                        label = AffiliatePromotionBottomSheetParams.SSAInfo.Label(
                            labelType = item?.ssaShopDetail?.label?.labelType.orEmpty(),
                            labelText = item?.ssaShopDetail?.label?.labelText.orEmpty()
                        )
                    )
                )
            }
        }
    }

    private fun sendClickEvent(item: AffiliateSSAShopListResponse.Data.SSAShop.ShopDataItem?) {
        AffiliateAnalytics.trackEventImpression(
            AffiliateAnalytics.EventKeys.SELECT_CONTENT,
            AffiliateAnalytics.ActionKeys.CLICK_SSA_SHOP_PAGE,
            AffiliateAnalytics.CategoryKeys.AFFILIATE_PROMOSIKAN_SSA_PAGE,
            UserSession(itemView.context).userId,
            item?.ssaShopDetail?.shopId.toString(),
            bindingAdapterPosition + 1,
            item?.ssaShopDetail?.shopName,
            "${item?.ssaShopDetail?.shopId}" +
                " - ${item?.ssaCommissionDetail?.cumulativePercentageFormatted}" +
                " - active" +
                " - komisi extra",
            AffiliateAnalytics.ItemKeys.AFFILIATE_SSA_SHOP_CLICK
        )
    }
}
