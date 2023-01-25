package com.tokopedia.affiliate.ui.viewholder

import android.view.View
import android.widget.ImageView
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.AVAILABLE
import com.tokopedia.affiliate.AffiliateAnalytics
import com.tokopedia.affiliate.PAGE_TYPE_SHOP
import com.tokopedia.affiliate.interfaces.ProductClickInterface
import com.tokopedia.affiliate.model.response.AffiliateSSAShopListResponse
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateSSAShopUiModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.loadImageRounded
import com.tokopedia.unifycomponents.DividerUnify
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

    override fun bind(element: AffiliateSSAShopUiModel?) {
        element?.ssaShop?.let {
            itemView.findViewById<ImageView>(R.id.imageMain)
                .loadImageRounded(it.ssaShopDetail?.imageURL?.androidURL)
            itemView.findViewById<Typography>(R.id.textViewTitle).text = it.ssaShopDetail?.shopName
            if (it.ssaShopDetail?.badgeURL?.isNotEmpty() == true) {
                itemView.findViewById<ImageView>(R.id.imageTitleEmblem).apply {
                    visible()
                    loadImage(it.ssaShopDetail.badgeURL)
                }
            }
            itemView.findViewById<Typography>(R.id.ssa_message).apply {
                visible()
                text = it.ssaShopDetail?.message
            }

            setUpFooterData(it.ssaShopDetail)
            setUpPromotionClickListener(it)
        }
    }

    private fun setUpFooterData(item: AffiliateSSAShopListResponse.Data.ShopDataItem.SSAShopDetail?) {
        item?.rating?.let { rating ->
            if (rating > 0) {
                itemView.findViewById<Typography>(R.id.textViewRating).apply {
                    visible()
                    text = item.rating.toString()
                }
                itemView.findViewById<ImageView>(R.id.imageRating).visible()
            }
        }

        itemView.findViewById<Typography>(R.id.textViewFooterLocation).apply {
            itemView.findViewById<ImageView>(R.id.imageFooter).visible()
            visible()
            text = item?.shopLocation
        }

        itemView.findViewById<Typography>(R.id.textViewItemSold).apply {
            visible()
            text = getString(R.string.affiliate_ssa_terjual, item?.quantitySold.toString())
            itemView.findViewById<DividerUnify>(R.id.ratingDivider).isVisible =
                item?.rating != null && item.rating > 0 && item.quantitySold != null
        }
    }

    private fun setUpPromotionClickListener(item: AffiliateSSAShopListResponse.Data.ShopDataItem?) {
        itemView.findViewById<UnifyButton>(R.id.buttonPromotion)?.run {
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
                    PAGE_TYPE_SHOP
                )
            }
        }
    }

    private fun sendClickEvent(item: AffiliateSSAShopListResponse.Data.ShopDataItem?) {
        AffiliateAnalytics.trackEventImpression(
            AffiliateAnalytics.EventKeys.SELECT_CONTENT,
            AffiliateAnalytics.ActionKeys.CLICK_SSA_SHOP_PAGE,
            AffiliateAnalytics.CategoryKeys.AFFILIATE_PROMOSIKAN_SSA_PAGE,
            UserSession(itemView.context).userId,
            item?.ssaShopDetail?.shopId.toString(),
            bindingAdapterPosition + 1,
            item?.ssaShopDetail?.shopName,
            "${item?.ssaShopDetail?.shopId} - ${item?.ssaCommissionDetail?.sellerPercentage} - active - komisi extra",
            AffiliateAnalytics.ItemKeys.AFFILIATE_SSA_SHOP_CLICK
        )
    }
}
