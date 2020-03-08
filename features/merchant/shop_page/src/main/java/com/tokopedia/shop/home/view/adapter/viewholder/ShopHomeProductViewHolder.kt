package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View

import androidx.annotation.LayoutRes

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.productcard.v2.BlankSpaceConfig
import com.tokopedia.productcard.v2.ProductCardModel
import com.tokopedia.productcard.v2.ProductCardViewSmallGrid
import com.tokopedia.shop.R
import com.tokopedia.shop.home.view.listener.ShopPageHomeProductClickListener
import com.tokopedia.shop.home.view.model.ShopHomeProductViewModel

import java.text.NumberFormat
import java.text.ParseException
import java.util.ArrayList

/**
 * @author by alvarisi on 12/12/17.
 */

open class ShopHomeProductViewHolder(
        itemView: View,
        private val shopPageHomeProductClickListener: ShopPageHomeProductClickListener?
) : AbstractViewHolder<ShopHomeProductViewModel>(itemView) {
    lateinit var productCard: ProductCardViewSmallGrid

    init {
        findViews(itemView)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_home_product_card
    }

    private fun findViews(view: View) {
        productCard = view.findViewById(R.id.product_card)
    }

    override fun bind(shopHomeProductViewModel: ShopHomeProductViewModel) {
        val totalReview = try {
            NumberFormat.getInstance().parse(shopHomeProductViewModel.totalReview).toInt()
        } catch (ignored: ParseException) {
            0
        }
        val discountPercentage = if (shopHomeProductViewModel.discountPercentage == "0") {
            ""
        } else {
            "${shopHomeProductViewModel.discountPercentage}%"
        }
        val freeOngkirObject = ProductCardModel.FreeOngkir(shopHomeProductViewModel.isShowFreeOngkir, shopHomeProductViewModel.freeOngkirPromoIcon!!)
        productCard.setProductModel(
                ProductCardModel(
                        shopHomeProductViewModel.imageUrl!!,
                        shopHomeProductViewModel.isWishList,
                        shopHomeProductViewModel.isShowWishList,
                        ProductCardModel.Label(),
                        "",
                        "",
                        shopHomeProductViewModel.name!!,
                        discountPercentage,
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
                },
                BlankSpaceConfig()
        )

//        if (isFixWidth && deviceWidth > 0 && layoutType == ShopHomeProductViewHolder.LAYOUT) {
//            itemView.layoutParams.width = (deviceWidth / RATIO_WITH_RELATIVE_TO_SCREEN).toInt()
//        }
//
//        productCard.setOnClickListener {
//            shopProductClickedListener?.onProductClicked(shopHomeProductViewModel, shopTrackType, adapterPosition)
//        }
//        productCard.setButtonWishlistOnClickListener {
//            if (!shopHomeProductViewModel.isSoldOut)
//                shopProductClickedListener?.onWishListClicked(shopHomeProductViewModel, shopTrackType)
//        }

        if (shopHomeProductViewModel.isCarousel) {
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
            if (shopHomeProductViewModel.discountPercentage.toIntOrZero() <= 0) {
                productCard.setlabelDiscountInvisible(true)
                productCard.setSlashedPriceInvisible(true)
            }
        }
    }
}