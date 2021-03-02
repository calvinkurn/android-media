package com.tokopedia.catalog.model.util

import com.tokopedia.catalog.model.raw.CatalogProductItem
import com.tokopedia.catalog.model.raw.ProductFreeOngkir
import com.tokopedia.catalog.model.raw.ProductListResponse
import com.tokopedia.catalog.model.raw.ProductShop
import com.tokopedia.common_category.model.topAds.TopAdsResponse

class CatalogProductListMapper {

    fun transform(productListResponse: ProductListResponse, topAdsResponse: TopAdsResponse)
            : ProductListResponse {

        if (productListResponse.searchProduct?.data?.catalogProductItemList?.isNotEmpty() == true) {

            topAdsResponse.productAds?.data?.let {
                for (i in it) {
                    i?.let { dataItem ->
                        val item = CatalogProductItem(
                                imageUrl300 = dataItem.product?.image?.sEcs ?: "",
                                        imageUrl = dataItem.product?.image?.sEcs ?: "",
                                imageUrl700 = dataItem.product?.image?.mEcs ?: "",
                                name = dataItem.product?.name ?: "",
                                shop = ProductShop(
                                    city = dataItem.shop.city,
                                    id = dataItem.shop.id,
                                    name = dataItem.shop.name
                                ),
                                discountPercentage = dataItem.product?.campaign?.discountPercentage
                                ?: 0,
                                originalPrice = dataItem.product?.campaign?.originalPrice ?: "",
                                priceRange = dataItem.product?.priceFormat ?: "",
                                price = dataItem.product?.priceFormat ?: "",
                                badgeList = dataItem.shop?.badges,
                                isTopAds = true,
                                ratingAverage = dataItem.product?.productRating.toString(),
                                countReview = getReviewCount(dataItem.product?.countReviewFormat
                                ?: "0"),
                                wishlist = (dataItem.product?.wishlist) ?: false,
                                id = (dataItem.product?.id?.toInt()) ?: 0,
                                categoryID = (dataItem.product?.category?.id?.toInt()) ?: 0,
                                productImpTrackingUrl = dataItem.product?.image?.sUrl ?: "",
                                productClickTrackingUrl = dataItem.productClickUrl ?: "",
                                productWishlistTrackingUrl = dataItem.productWishlistUrl ?: "",
                                freeOngkir = ProductFreeOngkir(dataItem.product?.freeOngkir?.isActive ?: false, dataItem.product?.freeOngkir?.imageUrl ?: "")
                        )

                        productListResponse.searchProduct.data.catalogProductItemList.add(0, item)
                    }
                }
            }
        }
        return productListResponse
    }

    private fun getReviewCount(s: String): Int {
        return try {
            val reviewCount = s.replace(".", "").replace(",", "")
            Integer.parseInt(reviewCount)
        } catch (e: NumberFormatException) {
            0
        }
    }
}
