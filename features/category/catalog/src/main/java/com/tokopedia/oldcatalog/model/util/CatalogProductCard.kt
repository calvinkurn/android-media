package com.tokopedia.oldcatalog.model.util

import com.tokopedia.common_category.model.productModel.BadgesItem
import com.tokopedia.oldcatalog.model.raw.CatalogProductItem
import com.tokopedia.oldcatalog.model.raw.ProductLabelGroup
import com.tokopedia.productcard.ProductCardModel

class CatalogProductCard {

    companion object {

        fun toCatalogProductModel(item: CatalogProductItem): ProductCardModel {
            return ProductCardModel(
                productImageUrl = item.imageUrl,
                productName = item.name,
                discountPercentage = if (item.discountPercentage > 0) "${item.discountPercentage}%" else "",
                slashedPrice = setSlashPrice(item.priceString, item.originalPrice),
                priceRange = item.priceString,
                labelGroupList = toGroupList(item.labelGroupList),
                shopBadgeList = toBadgeList(item.badgeList),
                shopLocation = item.shop.city,
                countSoldRating = item.ratingAverage,
                freeOngkir = ProductCardModel.FreeOngkir(
                    isActive = item.freeOngkir.isActive,
                    imageUrl = item.freeOngkir.imageUrl
                ),
                formattedPrice = item.priceString,
                isTopAds = item.isTopAds,
                hasThreeDots = true,
                isWishlisted = item.wishlist,
                shopName = item.shop.name,
                shopImageUrl = item.shop.url,
                hasAddToCartButton = true,
            )
        }

        private fun toBadgeList(badgeList: List<BadgesItem>): List<ProductCardModel.ShopBadge> {
            return arrayListOf<ProductCardModel.ShopBadge>().apply {
                badgeList.forEach {
                    add(ProductCardModel.ShopBadge(it.show, it.imageURL ?: ""))
                }
            }
        }

        private fun toGroupList(labelGroupList: List<ProductLabelGroup>): List<ProductCardModel.LabelGroup> {
            return arrayListOf<ProductCardModel.LabelGroup>().apply {
                labelGroupList.forEach {
                    add(
                        ProductCardModel.LabelGroup(
                            title = it.title, position = it.position,
                            type = it.type, imageUrl = it.url
                        )
                    )
                }
            }
        }

        private fun setSlashPrice(discountedPrice: String?, price: String?): String {
            if (discountedPrice.isNullOrEmpty()) {
                return ""
            } else if (discountedPrice == price) {
                return ""
            }
            return price ?: ""
        }
    }
}
