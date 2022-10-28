package com.tokopedia.shopdiscount.product_detail

import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.shopdiscount.common.data.request.DoSlashPriceReservationRequest
import com.tokopedia.shopdiscount.common.data.request.RequestHeader
import com.tokopedia.shopdiscount.common.data.response.DoSlashPriceProductReservationResponse
import com.tokopedia.shopdiscount.manage.data.response.DeleteDiscountResponse
import com.tokopedia.shopdiscount.product_detail.data.request.GetSlashPriceProductDetailRequest
import com.tokopedia.shopdiscount.product_detail.data.response.GetSlashPriceProductDetailResponse
import com.tokopedia.shopdiscount.product_detail.data.uimodel.ShopDiscountDetailReserveProductUiModel
import com.tokopedia.shopdiscount.product_detail.data.uimodel.ShopDiscountProductDetailDeleteUiModel
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

    fun mapToDoSlashPriceReservationRequest(
        requestId: String,
        productParentId: String,
        productParentPosition: Int
    ): DoSlashPriceReservationRequest {
        return DoSlashPriceReservationRequest(
            requestHeader = getRequestHeader(),
            action = DoSlashPriceReservationRequest.DoSlashPriceReservationAction.RESERVE,
            requestId = requestId,
            state = DoSlashPriceReservationRequest.DoSlashPriceReservationState.EDIT.toString(),
            listProductData = listOf(
                DoSlashPriceReservationRequest.SlashPriceReservationProduct(
                    productId = productParentId,
                    position = productParentPosition.toString()
                )
            )
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
                endDate = it.endDate,
                isVariant = it.isVariant
            )
        }
    }

    private fun getMinPriceDiscounted(productData: GetSlashPriceProductDetailResponse.GetSlashPriceProductDetail.ProductList): Int {
        return productData.warehouses.map {
            it.discountedPrice.toInt()
        }.toMutableList().apply {
            add(productData.discountedPrice.toInt())
        }.minOrNull().orZero()
    }

    private fun getMaxPriceDiscounted(productData: GetSlashPriceProductDetailResponse.GetSlashPriceProductDetail.ProductList): Int {
        return productData.warehouses.map {
            it.discountedPrice.toInt()
        }.toMutableList().apply {
            add(productData.discountedPrice.toInt())
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

    fun mapToShopDiscountDetailReserveProductUiModel(
        response: DoSlashPriceProductReservationResponse,
        requestId: String,
        selectedProductVariantId: String
    ): ShopDiscountDetailReserveProductUiModel {
        return ShopDiscountDetailReserveProductUiModel(
            responseHeader = response.doSlashPriceProductReservation.responseHeader,
            selectedProductVariantId = selectedProductVariantId,
            requestId = requestId
        )
    }

    fun mapToShopDiscountProductDetailDeleteUiModel(
        response: DeleteDiscountResponse,
        productId: String
    ): ShopDiscountProductDetailDeleteUiModel {
        return ShopDiscountProductDetailDeleteUiModel(
            responseHeader = response.doSlashPriceStop.responseHeader,
            productId = productId
        )
    }

}