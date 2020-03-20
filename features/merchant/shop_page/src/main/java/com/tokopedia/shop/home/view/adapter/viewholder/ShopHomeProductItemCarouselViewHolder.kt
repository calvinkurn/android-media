package com.tokopedia.shop.home.view.adapter.viewholder

import android.content.res.Resources
import android.view.View
import android.view.ViewGroup

import androidx.annotation.LayoutRes
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.productcard.v2.ProductCardModel

import com.tokopedia.shop.R
import com.tokopedia.shop.home.view.listener.ShopPageHomeProductClickListener
import com.tokopedia.shop.home.view.model.ShopHomeCarousellProductUiModel
import com.tokopedia.shop.home.view.model.ShopHomeCarousellProductUiModel.Companion.IS_ATC
import com.tokopedia.shop.home.view.model.ShopHomeProductViewModel
import java.text.NumberFormat
import java.text.ParseException

class ShopHomeProductItemCarouselViewHolder(
        itemView: View,
        private val shopPageHomeProductClickListener: ShopPageHomeProductClickListener?,
        private val shopHomeCarousellProductUiModel: ShopHomeCarousellProductUiModel?,
        private val parentIndex: Int
) : ShopHomeProductViewHolder(itemView, shopPageHomeProductClickListener) {

    companion object {
        const val RATIO_WITH_RELATIVE_TO_SCREEN = 2.3
        @LayoutRes
        val LAYOUT = R.layout.item_shop_home_product_card
    }

    override fun bind(shopHomeProductViewModel: ShopHomeProductViewModel) {
        super.bind(shopHomeProductViewModel)
        val deviceWidth = Resources.getSystem().displayMetrics.widthPixels;
        if (deviceWidth > 0) {
            itemView.layoutParams.width = (deviceWidth / RATIO_WITH_RELATIVE_TO_SCREEN).toInt()
        }
        itemView.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        productCard.setCardHeight(ViewGroup.LayoutParams.MATCH_PARENT)
        val isAtcFlag = shopHomeCarousellProductUiModel?.header?.isATC ?: 0
        productCard.setAddToCartVisible(isAtcFlag == IS_ATC)
    }

    override fun setListener() {
        productCard.setOnClickListener {
            shopPageHomeProductClickListener?.onCarouselProductItemClicked(
                    parentIndex,
                    adapterPosition,
                    shopHomeCarousellProductUiModel,
                    shopHomeProductViewModel
            )
        }

        shopHomeProductViewModel?.let {
            productCard.setImageProductViewHintListener(it, object : ViewHintListener {
                override fun onViewHint() {
                    shopPageHomeProductClickListener?.onCarouselProductItemImpression(
                            parentIndex,
                            adapterPosition,
                            shopHomeCarousellProductUiModel,
                            shopHomeProductViewModel
                    )
                }
            })
        }

        productCard.setButtonWishlistOnClickListener {
            shopHomeProductViewModel?.let {
                shopPageHomeProductClickListener?.onCarouselProductItemWishlist(
                        parentIndex,
                        adapterPosition,
                        shopHomeCarousellProductUiModel,
                        shopHomeProductViewModel
                )
            }

        }

        productCard.setAddToCartOnClickListener {
            shopPageHomeProductClickListener?.onCarouselProductItemClickAddToCart(
                    parentIndex,
                    adapterPosition,
                    shopHomeCarousellProductUiModel,
                    shopHomeProductViewModel
            )
        }
    }
}