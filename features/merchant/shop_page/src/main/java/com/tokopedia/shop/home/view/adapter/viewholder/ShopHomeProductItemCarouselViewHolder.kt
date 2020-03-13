package com.tokopedia.shop.home.view.adapter.viewholder

import android.content.res.Resources
import android.view.View

import androidx.annotation.LayoutRes
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.productcard.v2.ProductCardModel

import com.tokopedia.shop.R
import com.tokopedia.shop.home.view.listener.ShopPageHomeProductClickListener
import com.tokopedia.shop.home.view.model.ShopHomeCarousellProductUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductViewModel
import java.text.NumberFormat
import java.text.ParseException

/**
 * @author by alvarisi on 12/12/17.
 */

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
        val isAtcFlag = shopHomeCarousellProductUiModel?.header?.isATC ?: 0
        productCard.setAddToCartVisible(isAtcFlag == 1)
        val totalReview = try {
            NumberFormat.getInstance().parse(shopHomeProductViewModel.totalReview).toInt()
        } catch (ignored: ParseException) {
            0
        }
        val freeOngkirObject = ProductCardModel.FreeOngkir(shopHomeProductViewModel.isShowFreeOngkir, shopHomeProductViewModel.freeOngkirPromoIcon!!)
        if (shopHomeProductViewModel.rating <= 0 && totalReview <= 0) {
            productCard.setImageRatingInvisible(true)
            productCard.setReviewCountInvisible(true)
        }

        if (!freeOngkirObject.isActive || freeOngkirObject.imageUrl.isEmpty()) {
            productCard.setFreeOngkirInvisible(true)
        }
        if (!shopHomeProductViewModel.isPo && !shopHomeProductViewModel.isWholesale) {
            productCard.setLabelPreOrderInvisible(true)
        }
        if (shopHomeProductViewModel.discountPercentage?.replace("%", "").toIntOrZero() <= 0) {
            productCard.setlabelDiscountInvisible(true)
            productCard.setSlashedPriceInvisible(true)
        }
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

    override fun getProductModel(shopHomeProductViewModel: ShopHomeProductViewModel): ProductCardModel {
        val totalReview = try {
            NumberFormat.getInstance().parse(shopHomeProductViewModel.totalReview).toInt()
        } catch (ignored: ParseException) {
            0
        }
        val freeOngkirObject = ProductCardModel.FreeOngkir(shopHomeProductViewModel.isShowFreeOngkir, shopHomeProductViewModel.freeOngkirPromoIcon!!)
        return ProductCardModel(
                shopHomeProductViewModel.imageUrl!!,
                shopHomeProductViewModel.isWishList,
                shopHomeProductViewModel.isShowWishList,
                ProductCardModel.Label(),
                "",
                "",
                shopHomeProductViewModel.name!!,
                shopHomeProductViewModel.discountPercentage ?: "",
                shopHomeProductViewModel.originalPrice!!,
                shopHomeProductViewModel.displayedPrice!!,
                ArrayList(),
                "",
                shopHomeProductViewModel.rating.toInt(),
                totalReview,
                ProductCardModel.Label(),
                ProductCardModel.Label(),
                freeOngkirObject,
                false
        ).apply {
            isProductSoldOut = shopHomeProductViewModel.isSoldOut
            isProductPreOrder = shopHomeProductViewModel.isPo
            isProductWholesale = shopHomeProductViewModel.isWholesale
        }
    }
}