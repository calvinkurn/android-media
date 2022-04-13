package com.tokopedia.shopdiscount.product_detail

import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.orZero
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
                minOriginalPrice = it.price.min,
                maxOriginalPrice = it.price.max,
                minPriceDiscounted = getMinPriceDiscounted(it),
                maxPriceDiscounted = getMaxPriceDiscounted(it),
                minDiscount = getMinDiscount(it),
                maxDiscount = getMaxDiscount(it),
                stock = it.stock,
                totalLocation = it.warehouses.size,
                startDate = it.startDate,
                endDate = it.endDate
            )
        }
    }

    private fun getMinPriceDiscounted(productData: GetSlashPriceProductDetailResponse.GetSlashPriceProductDetail.ProductList): Int {
        return productData.warehouses.map {
            it.discountedPrice
        }.toMutableList().apply {
            add(productData.discountedPrice)
        }.minOrNull().orZero()
    }

    private fun getMaxPriceDiscounted(productData: GetSlashPriceProductDetailResponse.GetSlashPriceProductDetail.ProductList): Int {
        return productData.warehouses.map {
            it.discountedPrice
        }.toMutableList().apply {
            add(productData.discountedPrice)
        }.maxOrNull().orZero()
    }

    private fun getMinDiscount(productData: GetSlashPriceProductDetailResponse.GetSlashPriceProductDetail.ProductList): Int {
        return productData.warehouses.map {
            it.discountedPercentage
        }.toMutableList().apply {
            add(productData.discountedPercentage)
        }.minOrNull().orZero()
    }

    private fun getMaxDiscount(it: GetSlashPriceProductDetailResponse.GetSlashPriceProductDetail.ProductList): Int {
        return it.warehouses.map {
            it.discountedPercentage
        }.toMutableList().apply {
            add(it.discountedPercentage)
        }.maxOrNull().orZero()
    }

}