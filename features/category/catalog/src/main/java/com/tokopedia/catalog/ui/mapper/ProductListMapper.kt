package com.tokopedia.catalog.ui.mapper

import com.tokopedia.catalog.domain.model.CatalogProductItem
import com.tokopedia.catalog.domain.model.ProductLabelGroup
import com.tokopedia.catalog.ui.model.CatalogProductAtcUiModel
import com.tokopedia.common_category.model.productModel.BadgesItem
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.productcard.ProductCardModel

class ProductListMapper {

    companion object {

        fun mapperToCatalogProductModel(item: CatalogProductItem?): ProductCardModel {
            return ProductCardModel(
                productImageUrl = item?.imageUrl.orEmpty(),
                productName = item?.name.orEmpty(),
                discountPercentage = if (item?.discountPercentage.orZero() > 0) "${item?.discountPercentage.orZero()}%" else "",
                slashedPrice = setSlashPrice(
                    item?.priceString.orEmpty(),
                    item?.originalPrice.orEmpty()
                ),
                priceRange = item?.priceString.orEmpty(),
                labelGroupList = toGroupList(item?.labelGroupList.orEmpty()),
                shopBadgeList = toBadgeList(item?.badgeList.orEmpty()),
                shopLocation = item?.shop?.name.orEmpty(),
                countSoldRating = item?.ratingAverage.orEmpty(),
                freeOngkir = ProductCardModel.FreeOngkir(
                    isActive = item?.freeOngkir?.isActive.orFalse(),
                    imageUrl = item?.freeOngkir?.imageUrl.orEmpty()
                ),
                formattedPrice = item?.priceString.orEmpty(),
                isTopAds = item?.isTopAds.orFalse(),
                isWishlisted = item?.wishlist.orFalse(),
                shopName = item?.shop?.name.orEmpty(),
                shopImageUrl = item?.shop?.url.orEmpty(),
                hasAddToCartButton = true,
            )
        }

        fun mapperToCatalogProductAtcUiModel(item: CatalogProductItem): CatalogProductAtcUiModel {
            return CatalogProductAtcUiModel(
                productId = item.id,
                shopId = item.shop.id,
                quantity = item.minOrder,
                isVariant = item.childs.isNotEmpty(),
                warehouseId = item.warehouseIdDefault,
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
