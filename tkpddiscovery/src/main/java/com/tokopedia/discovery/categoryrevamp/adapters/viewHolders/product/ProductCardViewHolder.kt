package com.tokopedia.discovery.categoryrevamp.adapters.viewHolders.product

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.discovery.categoryrevamp.data.productModel.BadgesItem
import com.tokopedia.discovery.categoryrevamp.data.productModel.FreeOngkir
import com.tokopedia.discovery.categoryrevamp.data.productModel.LabelGroupsItem
import com.tokopedia.discovery.categoryrevamp.data.productModel.ProductsItem
import com.tokopedia.discovery.categoryrevamp.view.interfaces.ProductCardListener
import com.tokopedia.productcard.ProductCardModel
import kotlin.math.roundToInt

abstract class ProductCardViewHolder(itemView: View,
                                     var productListener: ProductCardListener) : AbstractViewHolder<ProductsItem>(itemView) {

    protected val context = itemView.context!!

    protected fun ProductsItem.toProductCardModel(isUsingBigImageUrl: Boolean): ProductCardModel {
        return ProductCardModel(
                productImageUrl = if (isUsingBigImageUrl) imageURL700 else imageURL,
                productName = name,
                discountPercentage = if (discountPercentage > 0) "$discountPercentage%" else "",
                slashedPrice = if (discountPercentage > 0) originalPrice else "",
                formattedPrice = price,
                priceRange = priceRange,
                shopBadgeList = badges.toProductCardModelShopBadges(),
                shopLocation = shop.location,
                ratingCount = rating.toRatingCount(isTopAds),
                reviewCount = countReview,
                freeOngkir = freeOngkir?.toProductCardModelFreeOngkir()
                        ?: ProductCardModel.FreeOngkir(),
                isTopAds = isTopAds,
                labelGroupList = labelGroups.toProductCardModelLabelGroup(),
                hasThreeDots = productListener.hasThreeDots()
        )
    }

    private fun List<BadgesItem>?.toProductCardModelShopBadges(): List<ProductCardModel.ShopBadge> {
        return this?.map {
            ProductCardModel.ShopBadge(isShown = it.show, imageUrl = it.imageURL ?: "")
        } ?: listOf()
    }

    private fun Int.toRatingCount(isTopAds: Boolean): Int {
        return if (isTopAds)
            (this / 20f).roundToInt()
        else
            this
    }

    private fun FreeOngkir.toProductCardModelFreeOngkir(): ProductCardModel.FreeOngkir {
        return ProductCardModel.FreeOngkir(isActive, imageUrl)
    }

    private fun List<LabelGroupsItem?>?.toProductCardModelLabelGroup(): List<ProductCardModel.LabelGroup> {
        return this?.map {
            ProductCardModel.LabelGroup(position = it?.position ?: "", title = it?.title ?: "", type = it?.type ?: "")
        } ?: listOf()
    }

}