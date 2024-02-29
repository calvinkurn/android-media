package com.tokopedia.catalog.ui.mapper

import com.tokopedia.catalog.domain.model.CatalogProductItem
import com.tokopedia.catalog.domain.model.CatalogProductListResponse
import com.tokopedia.catalog.domain.model.ProductLabelGroup
import com.tokopedia.catalog.ui.model.CatalogProductAtcUiModel
import com.tokopedia.catalog.ui.model.CatalogProductListUiModel
import com.tokopedia.common_category.model.productModel.BadgesItem
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.productcard.ProductCardModel

class ProductListMapper {

    companion object {
        fun CatalogProductListResponse.map() : CatalogProductListUiModel{
            val dataProduct = this.catalogGetProductList
            return CatalogProductListUiModel(
                header = CatalogProductListUiModel.HeaderUiModel(dataProduct.header.totalData),
                products = dataProduct.products.map {
                    CatalogProductListUiModel.CatalogProductUiModel(
                        additionalService = CatalogProductListUiModel.CatalogProductUiModel.AdditionalServiceUiModel(
                            it.additionalService?.name.orEmpty()
                        ),
                        credibility = CatalogProductListUiModel.CatalogProductUiModel.CredibilityUiModel(
                            it.credibility?.rating.orEmpty(),
                            it.credibility?.ratingCount.orEmpty(),
                            it.credibility?.sold.orEmpty()
                        ),
                        delivery = CatalogProductListUiModel.CatalogProductUiModel.DeliveryUiModel(
                            it.delivery?.eta.orEmpty(),
                            it.delivery?.type.orEmpty()
                        ),
                        isVariant = it.isVariant.orFalse(),
                        labelGroups = it.labelGroups?.map {
                            CatalogProductListUiModel.CatalogProductUiModel.LabelGroupUiModel(
                                it.position.orEmpty(),
                                it.title.orEmpty(),
                                it.url.orEmpty()
                            )
                        }.orEmpty(),
                        mediaUrl = CatalogProductListUiModel.CatalogProductUiModel.MediaUrlUiModel(
                            it.mediaUrl?.image.orEmpty(),
                            it.mediaUrl?.image300.orEmpty(),
                            it.mediaUrl?.image500.orEmpty(),
                            it.mediaUrl?.image700.orEmpty()
                        ),
                        paymentOption = CatalogProductListUiModel.CatalogProductUiModel.PaymentOptionUiModel(
                            it.paymentOption?.desc.orEmpty(),
                            it.paymentOption?.iconUrl.orEmpty()
                        ),
                        price = CatalogProductListUiModel.CatalogProductUiModel.PriceUiModel(
                            it.price?.original.orEmpty(),
                            it.price?.text.orEmpty()
                        ),
                        productID = it.productID.orEmpty(),
                        shop = CatalogProductListUiModel.CatalogProductUiModel.ShopUiModel(
                            it.shop?.badge.orEmpty(), it.shop?.city.orEmpty(), it.shop?.id.orEmpty(), it.shop?.name.orEmpty()
                        ),
                        warehouseID = it.warehouseID.orEmpty(),
                        stock = CatalogProductListUiModel.CatalogProductUiModel.StockUiModel(
                            it.stock?.isHidden.orFalse(), it.stock?.soldPercentage.orZero(), it.stock?.wording.orEmpty()
                        )

                    )
                }
            )
        }

        fun mapperToCatalogProductModel(item: CatalogProductItem?): ProductCardModel {
            return ProductCardModel(
                productImageUrl = item?.imageUrl.orEmpty(),
                productName = item?.name.orEmpty(),
                discountPercentage = if (item?.discountPercentage.orZero() > 0) "${item?.discountPercentage.orZero()}%" else "",
                slashedPrice = setSlashPrice(
                    item?.priceString.orEmpty(),
                    item?.originalPrice.orEmpty()
                ),
                priceRange = item?.priceRange.orEmpty(),
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
                hasAddToCartButton = true
            )
        }

        fun mapperToCatalogProductAtcUiModel(item: CatalogProductListUiModel.CatalogProductUiModel): CatalogProductAtcUiModel {
            return CatalogProductAtcUiModel(
                productId = item.productID,
                shopId = item.shop.id,
                quantity = 1,
                isVariant = item.isVariant.orFalse(),
                warehouseId = item.warehouseID
            )
        }

        fun mapperToCatalogProductAtcUiModel(item: CatalogProductItem): CatalogProductAtcUiModel {
            return CatalogProductAtcUiModel(
                productId = item.id,
                shopId = item.shop.id,
                quantity = item.minOrder,
                isVariant = item.childs.isNotEmpty(),
                warehouseId = item.warehouseIdDefault
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
                            title = it.title,
                            position = it.position,
                            type = it.type,
                            imageUrl = it.url
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
