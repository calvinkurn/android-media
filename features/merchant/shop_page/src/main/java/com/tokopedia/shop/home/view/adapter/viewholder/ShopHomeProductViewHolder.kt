package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View

import androidx.annotation.LayoutRes

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ViewHintListener
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
    protected var shopHomeProductViewModel: ShopHomeProductViewModel? = null

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
        this.shopHomeProductViewModel = shopHomeProductViewModel
        productCard.setProductModel(getProductModel(shopHomeProductViewModel), BlankSpaceConfig())
        setListener()
    }

    protected open fun setListener() {
        productCard.setOnClickListener {
            shopPageHomeProductClickListener?.onAllProductItemClicked(
                    adapterPosition,
                    shopHomeProductViewModel
            )
        }
        shopHomeProductViewModel?.let {
            productCard.setImageProductViewHintListener(it, object : ViewHintListener {
                override fun onViewHint() {
                    shopPageHomeProductClickListener?.onAllProductItemImpression(
                            adapterPosition,
                            shopHomeProductViewModel
                    )
                }
            })
        }
        productCard.setButtonWishlistOnClickListener {
            shopHomeProductViewModel?.let {
                shopPageHomeProductClickListener?.onAllProductItemWishlist(
                        adapterPosition,
                        it
                )
            }

        }
    }

    private fun getProductModel(
            shopHomeProductViewModel: ShopHomeProductViewModel
    ): ProductCardModel {
        val totalReview = shopHomeProductViewModel.totalReview.toIntOrZero()
        val discountWithoutPercentageString = shopHomeProductViewModel.discountPercentage?.replace("%","") ?: ""
        val discountPercentage = if (discountWithoutPercentageString == "0") {
            ""
        } else {
            "$discountWithoutPercentageString%"
        }
        val freeOngkirObject = ProductCardModel.FreeOngkir(shopHomeProductViewModel.isShowFreeOngkir, shopHomeProductViewModel.freeOngkirPromoIcon  ?: "")
        return ProductCardModel(
                shopHomeProductViewModel.imageUrl ?: "",
                shopHomeProductViewModel.isWishList,
                shopHomeProductViewModel.isShowWishList,
                ProductCardModel.Label(),
                "",
                "",
                shopHomeProductViewModel.name ?: "",
                discountPercentage,
                shopHomeProductViewModel.originalPrice ?: "",
                shopHomeProductViewModel.displayedPrice ?: "",
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