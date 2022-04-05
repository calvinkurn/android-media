package com.tokopedia.shopdiscount.product_detail

import com.tokopedia.shopdiscount.common.data.request.RequestHeader
import com.tokopedia.shopdiscount.product_detail.data.request.GetSlashPriceProductDetailRequest
import com.tokopedia.shopdiscount.product_detail.data.response.GetSlashPriceProductDetailResponse
import com.tokopedia.shopdiscount.product_detail.data.uimodel.ShopDiscountProductDetailUiModel

object ShopDiscountProductDetailMapper {

    fun getGetSlashPriceProductDetailRequestData(
        productId: String,
        status: Int
    ): GetSlashPriceProductDetailRequest {
        return GetSlashPriceProductDetailRequest(
            requestHeader = getRequestHeader(),
            slashPriceProductDetailFilter = getSlashPriceProductDetailFilter(productId, status)
        )
    }

    private fun getSlashPriceProductDetailFilter(
        productId: String,
        status: Int
    ): GetSlashPriceProductDetailRequest.SlashPriceProductDetailFilter {
        return GetSlashPriceProductDetailRequest.SlashPriceProductDetailFilter(
            listProductId = listOf(productId),
            status = status
        )
    }

    private fun getRequestHeader(): RequestHeader {
        return RequestHeader(
            source = "",
            ip = "",
            usecase = ""
        )
    }

    fun mapToShopDiscountProductDetailUiModel(
        response: GetSlashPriceProductDetailResponse.GetSlashPriceProductDetail
    ): ShopDiscountProductDetailUiModel {
        return ShopDiscountProductDetailUiModel(
            responseHeader = response.responseHeader,
            listProductDetailData = mapToListProductDetailData(response.productList)
        )
    }

    private fun mapToListProductDetailData(
        productList: List<GetSlashPriceProductDetailResponse.GetSlashPriceProductDetail.ProductList>
    ): List<ShopDiscountProductDetailUiModel.ProductDetailData> {
        return productList.map {
            ShopDiscountProductDetailUiModel.ProductDetailData(
                productId = it.productId,
                productName = it.name,
                productImageUrl = it.picture,
                minPrice = it.price.minFormatted,
                maxPrice = it.price.maxFormatted,
                minPriceDiscounted = "",
                maxPriceDiscounted = "",
                minDiscount = "",
                maxDiscount = "",
                stock = it.stock,
                totalLocation = it.warehouses.size,
                startDate = it.startDate,
                endDate = it.endDate
            )
        }
    }

}