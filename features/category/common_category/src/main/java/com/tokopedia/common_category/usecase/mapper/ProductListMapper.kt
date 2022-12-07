package com.tokopedia.common_category.usecase.mapper

import com.tokopedia.common_category.model.productModel.FreeOngkir
import com.tokopedia.common_category.model.productModel.ProductListResponse
import com.tokopedia.common_category.model.productModel.ProductsItem
import com.tokopedia.common_category.model.productModel.Shop
import com.tokopedia.common_category.model.topAds.TopAdsResponse

class ProductListMapper {

    fun transform(productListResponse: ProductListResponse, topAdsResponse: TopAdsResponse)
            : ProductListResponse {

        if (productListResponse.searchProduct?.products?.isNotEmpty() == true) {

            topAdsResponse.productAds?.data?.let {
                for (i in it) {
                    i?.let { dataItem ->
                        val item = ProductsItem(
                                imageURL300 = dataItem.product?.image?.sEcs ?: "",
                                        imageURL = dataItem.product?.image?.sEcs ?: "",
                                imageURL700 = dataItem.product?.image?.mEcs ?: "",
                                name = dataItem.product?.name ?: "",
                                shop = Shop(
                                    city = dataItem.shop.city,
                                    id = dataItem.shop.id.toInt(),
                                    location = dataItem.shop.location,
                                    goldmerchant = dataItem.shop.goldShop,
                                    name = dataItem.shop.name
                                ),
                                discountPercentage = dataItem.product?.campaign?.discountPercentage
                                ?: 0,
                                originalPrice = dataItem.product?.campaign?.originalPrice ?: "",
                                priceRange = dataItem.product?.priceFormat ?: "",
                                price = dataItem.product?.priceFormat ?: "",
                                badges = dataItem.shop?.badges,
                                isTopAds = true,
                                rating = dataItem.product?.productRating ?: 0,
                                countReview = getReviewCount(dataItem.product?.countReviewFormat
                                ?: "0"),
                                wishlist = (dataItem.product?.wishlist) ?: false,
                                id = (dataItem.product?.id) ?: "",
                                categoryID = (dataItem.product?.category?.id?.toInt()) ?: 0,
                                productImpTrackingUrl = dataItem.product?.image?.sUrl ?: "",
                                productClickTrackingUrl = dataItem.productClickUrl ?: "",
                                productWishlistTrackingUrl = dataItem.productWishlistUrl ?: "",
                                freeOngkir = FreeOngkir(dataItem.product?.freeOngkir?.isActive ?: false, dataItem.product?.freeOngkir?.imageUrl ?: "")
                        )

                        productListResponse.searchProduct.products.add(0, item)
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
