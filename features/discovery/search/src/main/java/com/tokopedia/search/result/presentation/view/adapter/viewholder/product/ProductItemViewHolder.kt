package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.productcard.v2.ProductCardModel
import com.tokopedia.search.result.presentation.model.BadgeItemViewModel
import com.tokopedia.search.result.presentation.model.FreeOngkirViewModel
import com.tokopedia.search.result.presentation.model.ProductItemViewModel
import com.tokopedia.search.result.presentation.view.listener.ProductListener
import kotlin.math.roundToInt

abstract class ProductItemViewHolder(
        itemView: View,
        protected val productListener: ProductListener
) : AbstractViewHolder<ProductItemViewModel>(itemView) {

    protected val context = itemView.context!!

    fun ProductItemViewModel.toProductCardModel(isUsingBigImageUrl: Boolean): ProductCardModel {
        return ProductCardModel(
                productImageUrl = if (isUsingBigImageUrl) imageUrl700 else imageUrl,
                productName = productName,
                discountPercentage = if (discountPercentage > 0) "$discountPercentage%" else "",
                slashedPrice = if (discountPercentage > 0) originalPrice else "",
                formattedPrice = price,
                priceRange = priceRange ?: "",
                shopBadgeList = badgesList.toProductCardModelShopBadges(),
                shopLocation = shopCity,
                ratingCount = rating.toRatingCount(isTopAds),
                reviewCount = countReview,
                freeOngkir = freeOngkirViewModel.toProductCardModelFreeOngkir(),
                isTopAds = isTopAds,
                ratingString = "", // TODO:: Wait for backend to be ready
                hasOptions = true,
                labelGroupList = listOf() // TODO:: Wait for backend to be ready
        )
    }

    private fun List<BadgeItemViewModel>.toProductCardModelShopBadges(): List<ProductCardModel.ShopBadge> {
        val shopBadgeList = mutableListOf<ProductCardModel.ShopBadge>()

        forEach {
            shopBadgeList.add(ProductCardModel.ShopBadge(it.isShown, it.imageUrl))
        }

        return shopBadgeList
    }

    private fun Int.toRatingCount(isTopAds: Boolean): Int {
        return if (isTopAds)
            (this / 20f).roundToInt()
        else
            this
    }

    private fun FreeOngkirViewModel.toProductCardModelFreeOngkir(): ProductCardModel.FreeOngkir {
        return ProductCardModel.FreeOngkir(isActive, imageUrl)
    }

    protected fun createImageProductViewHintListener(productItem: ProductItemViewModel): ViewHintListener {
        return object: ViewHintListener {
            override fun onViewHint() {
                productListener.onProductImpressed(productItem, adapterPosition)
            }
        }
    }
}