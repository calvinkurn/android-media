package com.tokopedia.shopdiscount.product_detail

import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.shopdiscount.common.data.request.DoSlashPriceReservationRequest
import com.tokopedia.shopdiscount.common.data.request.RequestHeader
import com.tokopedia.shopdiscount.common.data.response.DoSlashPriceProductReservationResponse
import com.tokopedia.shopdiscount.manage.data.response.DeleteDiscountResponse
import com.tokopedia.shopdiscount.product_detail.data.request.GetSlashPriceProductDetailRequest
import com.tokopedia.shopdiscount.product_detail.data.response.GetSlashPriceProductDetailResponse
import com.tokopedia.shopdiscount.product_detail.data.uimodel.ShopDiscountDetailReserveProductUiModel
import com.tokopedia.shopdiscount.product_detail.data.uimodel.ShopDiscountProductDetailDeleteUiModel
import com.tokopedia.shopdiscount.product_detail.data.uimodel.ShopDiscountProductDetailUiModel
import com.tokopedia.shopdiscount.subsidy.model.uimodel.ShopDiscountProductRuleUiModel
import com.tokopedia.shopdiscount.subsidy.model.uimodel.ShopDiscountSubsidyInfoUiModel
import com.tokopedia.shopdiscount.utils.constant.DateConstant
import com.tokopedia.shopdiscount.utils.extension.parseTo
import com.tokopedia.shopdiscount.utils.extension.toDate

object ShopDiscountProductDetailMapper {

    private const val END_DATE_FORMAT = "dd MMM yyyy HH:mm z"
    fun getGetSlashPriceProductDetailRequestData(
        listProductId: List<String>,
        status: Int,
        isValidateSizeVariant: Boolean
    ): GetSlashPriceProductDetailRequest {
        return GetSlashPriceProductDetailRequest(
            requestHeader = getRequestHeader(),
            slashPriceProductDetailFilter = getSlashPriceProductDetailFilter(listProductId, status),
            isValidateSizeVariant = isValidateSizeVariant
        )
    }

    private fun getSlashPriceProductDetailFilter(
        listProductId: List<String>,
        status: Int
    ): GetSlashPriceProductDetailRequest.SlashPriceProductDetailFilter {
        return GetSlashPriceProductDetailRequest.SlashPriceProductDetailFilter(
            listProductId = listProductId,
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
                isParent = it.parentInfo.isParent,
                parentName = it.parentInfo.name,
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
                isVariant = it.isVariant,
                maxOrder = it.maxOrder.toIntOrZero(),
                parentId = it.parentId,
                isSubsidy = it.joinSubsidy,
                subsidyStatusText = it.subsidyStatusText,
                productRule = ShopDiscountProductRuleUiModel(
                    isAbleToOptOut = it.rule.isAbleToOptOut
                ),
                subsidyInfo = ShopDiscountSubsidyInfoUiModel(
                    ctaProgramLink = it.subsidyInfo.ctaProgramLink,
                    subsidyType = ShopDiscountSubsidyInfoUiModel.getSubsidyType(it.subsidyInfo.subsidyType),
                    discountedPrice = it.subsidyInfo.discountedPrice,
                    discountedPercentage = it.subsidyInfo.discountedPercentage,
                    remainingQuota = it.subsidyInfo.remainingQuota,
                    quotaSubsidy = it.subsidyInfo.quotaSubsidy,
                    maxOrder = it.subsidyInfo.maxOrder,
                    subsidyDateStart = it.subsidyInfo.subsidyDateStart,
                    subsidyDateEnd = it.subsidyInfo.subsidyDateEnd,
                    sellerDiscountPrice = it.subsidyInfo.sellerDiscountPrice,
                    sellerDiscountPercentage = it.subsidyInfo.sellerDiscountPercentage,
                    minOriginalPriceSubsidy = it.warehouses.getMinOriginalPriceSubsidy(),
                    maxOriginalPriceSubsidy = it.warehouses.getMaxOriginalPriceSubsidy(),
                    minSellerDiscountPriceSubsidy = it.warehouses.getMinSellerDiscountPriceSubsidy(),
                    maxSellerDiscountPriceSubsidy = it.warehouses.getMaxSellerDiscountPriceSubsidy(),
                    minSellerDiscountPercentageSubsidy = it.warehouses.getMinSellerDiscountPercentageSubsidy(),
                    maxSellerDiscountPercentageSubsidy = it.warehouses.getMaxSellerDiscountPercentageSubsidy(),
                    minProgramDiscountPriceSubsidy = it.warehouses.getMinProgramDiscountPriceSubsidy(),
                    maxProgramDiscountPriceSubsidy = it.warehouses.getMaxProgramDiscountPriceSubsidy(),
                    minProgramDiscountPercentageSubsidy = it.warehouses.getMinProgramDiscountPercentageSubsidy(),
                    maxProgramDiscountPercentageSubsidy = it.warehouses.getMaxProgramDiscountPercentageSubsidy(),
                    minFinalDiscountPriceSubsidy = it.warehouses.getMinFinalDiscountPriceSubsidy(),
                    maxFinalDiscountPriceSubsidy = it.warehouses.getMaxFinalDiscountPriceSubsidy(),
                    minFinalDiscountPercentageSubsidy = it.warehouses.getMinFinalDiscountPercentageSubsidy(),
                    maxFinalDiscountPercentageSubsidy = it.warehouses.getMaxFinalDiscountPercentageSubsidy(),
                ),
                eventId = it.warehouses.firstOrNull()?.eventId.orEmpty(),
                isMultiWarehouse = it.warehouses.size > Int.ONE
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

    private fun String.formatStartDate(): String {
        return this.toDate(DateConstant.DATE_TIME_SECOND_PRECISION_WITH_TIMEZONE)
            .parseTo(DateConstant.DATE_TIME_MINUTE_PRECISION)
    }

    private fun String.formatEndDate(): String {
        return this.toDate(DateConstant.DATE_TIME_SECOND_PRECISION_WITH_TIMEZONE)
            .parseTo(END_DATE_FORMAT)
    }

    
    private fun List<GetSlashPriceProductDetailResponse.GetSlashPriceProductDetail.ProductList.Warehouses>.getMinOriginalPriceSubsidy(): Int {
        return this.filter { it.joinSubsidy }.map {
            it.originalPrice.toInt()
        }.toMutableList().minOrNull().orZero()
    }

    private fun List<GetSlashPriceProductDetailResponse.GetSlashPriceProductDetail.ProductList.Warehouses>.getMaxOriginalPriceSubsidy(): Int {
        return this.filter { it.joinSubsidy }.map {
            it.originalPrice.toInt()
        }.toMutableList().maxOrNull().orZero()
    }

    private fun List<GetSlashPriceProductDetailResponse.GetSlashPriceProductDetail.ProductList.Warehouses>.getMinSellerDiscountPriceSubsidy(): Int {
        return this.filter { it.joinSubsidy }.map {
            it.subsidyInfo.sellerDiscountPrice.toInt()
        }.toMutableList().minOrNull().orZero()
    }

    private fun List<GetSlashPriceProductDetailResponse.GetSlashPriceProductDetail.ProductList.Warehouses>.getMaxSellerDiscountPriceSubsidy(): Int {
        return this.filter { it.joinSubsidy }.map {
            it.subsidyInfo.sellerDiscountPrice.toInt()
        }.toMutableList().maxOrNull().orZero()
    }


    private fun List<GetSlashPriceProductDetailResponse.GetSlashPriceProductDetail.ProductList.Warehouses>.getMinSellerDiscountPercentageSubsidy(): Int {
        return this.filter { it.joinSubsidy }.map {
            it.subsidyInfo.sellerDiscountPercentage
        }.toMutableList().minOrNull().orZero()
    }

    private fun List<GetSlashPriceProductDetailResponse.GetSlashPriceProductDetail.ProductList.Warehouses>.getMaxSellerDiscountPercentageSubsidy(): Int {
        return this.filter { it.joinSubsidy }.map {
            it.subsidyInfo.sellerDiscountPercentage
        }.toMutableList().maxOrNull().orZero()
    }


    private fun List<GetSlashPriceProductDetailResponse.GetSlashPriceProductDetail.ProductList.Warehouses>.getMinProgramDiscountPriceSubsidy(): Int {
        return this.filter { it.joinSubsidy }.map {
            it.subsidyInfo.discountedPrice.toInt()
        }.toMutableList().minOrNull().orZero()
    }

    private fun List<GetSlashPriceProductDetailResponse.GetSlashPriceProductDetail.ProductList.Warehouses>.getMaxProgramDiscountPriceSubsidy(): Int {
        return this.filter { it.joinSubsidy }.map {
            it.subsidyInfo.discountedPrice.toInt()
        }.toMutableList().maxOrNull().orZero()
    }

    private fun List<GetSlashPriceProductDetailResponse.GetSlashPriceProductDetail.ProductList.Warehouses>.getMinProgramDiscountPercentageSubsidy(): Int {
        return this.filter { it.joinSubsidy }.map {
            it.subsidyInfo.discountedPercentage
        }.toMutableList().minOrNull().orZero()
    }

    private fun List<GetSlashPriceProductDetailResponse.GetSlashPriceProductDetail.ProductList.Warehouses>.getMaxProgramDiscountPercentageSubsidy(): Int {
        return this.filter { it.joinSubsidy }.map {
            it.subsidyInfo.discountedPercentage
        }.toMutableList().maxOrNull().orZero()
    }


    private fun List<GetSlashPriceProductDetailResponse.GetSlashPriceProductDetail.ProductList.Warehouses>.getMinFinalDiscountPriceSubsidy(): Int {
        return this.filter { it.joinSubsidy }.map {
            it.discountedPrice.toInt()
        }.toMutableList().minOrNull().orZero()
    }

    private fun List<GetSlashPriceProductDetailResponse.GetSlashPriceProductDetail.ProductList.Warehouses>.getMaxFinalDiscountPriceSubsidy(): Int {
        return this.filter { it.joinSubsidy }.map {
            it.discountedPrice.toInt()
        }.toMutableList().maxOrNull().orZero()
    }


    private fun List<GetSlashPriceProductDetailResponse.GetSlashPriceProductDetail.ProductList.Warehouses>.getMinFinalDiscountPercentageSubsidy(): Int {
        return this.filter { it.joinSubsidy }.map {
            it.discountedPercentage
        }.toMutableList().minOrNull().orZero()
    }

    private fun List<GetSlashPriceProductDetailResponse.GetSlashPriceProductDetail.ProductList.Warehouses>.getMaxFinalDiscountPercentageSubsidy(): Int {
        return this.filter { it.joinSubsidy }.map {
            it.discountedPercentage
        }.toMutableList().maxOrNull().orZero()
    }
}
