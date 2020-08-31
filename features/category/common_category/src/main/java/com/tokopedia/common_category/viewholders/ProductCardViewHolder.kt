package com.tokopedia.common_category.viewholders

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.common_category.constants.CategoryNavConstants
import com.tokopedia.common_category.interfaces.ProductCardListener
import com.tokopedia.common_category.model.productModel.BadgesItem
import com.tokopedia.common_category.model.productModel.FreeOngkir
import com.tokopedia.common_category.model.productModel.LabelGroupsItem
import com.tokopedia.common_category.model.productModel.ProductsItem
import com.tokopedia.productcard.ProductCardModel
import kotlin.math.roundToInt

abstract class ProductCardViewHolder(itemView: View,
                                     var productListener: ProductCardListener) : AbstractViewHolder<ProductsItem>(itemView) {

    protected val context = itemView.context!!

    protected fun ProductsItem.toProductCardModel(gridType: CategoryNavConstants.RecyclerView.GridType): ProductCardModel {
        return ProductCardModel(
                productImageUrl = when (gridType) {
                    CategoryNavConstants.RecyclerView.GridType.GRID_1 -> imageURL300 ?: ""
                    CategoryNavConstants.RecyclerView.GridType.GRID_2 -> imageURL
                    CategoryNavConstants.RecyclerView.GridType.GRID_3 -> imageURL700
                },
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
            ProductCardModel.LabelGroup(position = it?.position ?: "", title = it?.title
                    ?: "", type = it?.type ?: "")
        } ?: listOf()
    }

}