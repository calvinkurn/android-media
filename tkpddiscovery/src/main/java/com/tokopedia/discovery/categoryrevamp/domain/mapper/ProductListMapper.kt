package com.tokopedia.discovery.categoryrevamp.domain.mapper

import com.tokopedia.discovery.categoryrevamp.data.productModel.ProductListResponse
import com.tokopedia.discovery.categoryrevamp.data.productModel.ProductsItem
import com.tokopedia.discovery.categoryrevamp.data.topAds.TopAdsResponse

class ProductListMapper {

    fun transform(productListResponse: ProductListResponse, topAdsResponse: TopAdsResponse)
            : ProductListResponse {

        val response = productListResponse

        topAdsResponse.productAds?.data?.let {

            for (i in it) {
                val item = ProductsItem()

                i?.let { dataItem ->

                    item.imageURL300 = dataItem.product?.image?.sEcs ?: ""
                    item.imageURL = dataItem.product?.image?.sEcs ?: ""
                    item.name = dataItem.product?.name ?: ""
                    item.shop.name = dataItem.shop?.name ?: ""
                    item.discountPercentage = dataItem.product?.campaign?.discountPercentage ?: 0
                    item.originalPrice = dataItem.product?.campaign?.originalPrice ?: ""
                    item.priceRange = dataItem.product?.priceFormat ?: ""
                    item.price = dataItem.product?.priceFormat ?: ""
                    dataItem.shop?.let { shop ->
                        shop.badges?.let { badgeList ->
                            item.badges = badgeList
                        }
                    }
                    item.shop.city = dataItem.shop?.city ?: ""
                    item.isTopAds = true
                    item.rating = dataItem.product?.productRating ?: 0

                    item.countReview = (dataItem.product?.countReviewFormat?.toInt()) ?: 0
                    item.wishlist = (dataItem.product?.wishlist) ?: false

                    item.id = (dataItem.product?.id?.toInt()) ?: 0
                    item.categoryID = (dataItem.product?.category?.id?.toInt()) ?: 0
                }

                response.searchProduct?.products?.add(0, item)
            }


        }


        return response
    }
}