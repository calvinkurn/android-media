package com.tokopedia.shopdiscount.manage_discount.util

import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.shopdiscount.common.data.request.DoSlashPriceReservationRequest
import com.tokopedia.shopdiscount.common.data.request.RequestHeader
import com.tokopedia.shopdiscount.common.data.response.DoSlashPriceProductReservationResponse
import com.tokopedia.shopdiscount.manage_discount.data.request.DoSlashPriceProductSubmissionRequest
import com.tokopedia.shopdiscount.manage_discount.data.request.GetSlashPriceSetupProductListRequest
import com.tokopedia.shopdiscount.manage_discount.data.response.DoSlashPriceProductSubmissionResponse
import com.tokopedia.shopdiscount.manage_discount.data.response.GetSlashPriceSetupProductListResponse
import com.tokopedia.shopdiscount.manage_discount.data.uimodel.ShopDiscountSetupProductUiModel
import com.tokopedia.shopdiscount.manage_discount.data.uimodel.ShopDiscountSlashPriceProductSubmissionUiModel
import com.tokopedia.shopdiscount.manage_discount.data.uimodel.ShopDiscountSlashPriceStopUiModel
import com.tokopedia.shopdiscount.utils.constant.DateConstant
import com.tokopedia.utils.date.toDate
import java.lang.Exception
import java.util.Date

object ShopDiscountManageDiscountMapper {

    fun getSlashPriceSetupProductListRequestData(
        requestId: String
    ): GetSlashPriceSetupProductListRequest {
        return GetSlashPriceSetupProductListRequest(
            requestHeader = getRequestHeader(),
            requestId = requestId
        )
    }

    fun mapToShopDiscountSetupProductUiModel(
        response: GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList,
        selectedProductVariantId: String
    ): ShopDiscountSetupProductUiModel {
        return ShopDiscountSetupProductUiModel(
            responseHeader = response.responseHeader,
            listSetupProductData = mapToListSetupProductUiModel(
                response.productList,
                selectedProductVariantId
            )
        )
    }

    private fun getRequestHeader(): RequestHeader {
        return RequestHeader(
            ip = "",
            usecase = ""
        )
    }

    private fun mapToListSetupProductUiModel(
        listProductResponse: List<GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList.ProductList>,
        selectedProductVariantId: String
    ): List<ShopDiscountSetupProductUiModel.SetupProductData> {
        return listProductResponse.map {
            ShopDiscountSetupProductUiModel.SetupProductData(
                productId = it.productId,
                productName = it.name,
                productImageUrl = it.picture,
                stock = it.stock,
                listProductWarehouse = mapToListProductWarehouseData(it.warehouses),
                slashPriceInfo = mapToSlashPriceInfo(it.slashPriceInfo),
                price = mapToPriceData(it.price),
                listProductVariant = mapToListProductVariant(it, selectedProductVariantId)
            )
        }
    }

    private fun mapToPriceData(
        priceResponse: GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList.ProductList.Price
    ): ShopDiscountSetupProductUiModel.SetupProductData.ProductPrice {
        return ShopDiscountSetupProductUiModel.SetupProductData.ProductPrice(
            min = priceResponse.min,
            minFormatted = priceResponse.minFormatted,
            max = priceResponse.max,
            maxFormatted = priceResponse.maxFormatted
        )
    }

    private fun mapToSlashPriceInfo(
        slashPriceResponse: GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList.ProductList.SlashPriceInfo
    ): ShopDiscountSetupProductUiModel.SetupProductData.ProductSlashPriceInfo {
        return slashPriceResponse.let {
            var startDate = Date()
            var endDate = Date()
            try {
                startDate =
                    it.startDate.toDate(DateConstant.DATE_TIME_SECOND_PRECISION_WITH_TIMEZONE)
                endDate = it.endDate.toDate(DateConstant.DATE_TIME_SECOND_PRECISION_WITH_TIMEZONE)
            } catch (e: Exception) {
            }
            ShopDiscountSetupProductUiModel.SetupProductData.ProductSlashPriceInfo(
                slashPriceProductId = it.slashPriceProductId,
                discountedPrice = it.discountedPrice.toInt(),
                discountPercentage = it.discountPercentage,
                startDate = startDate,
                endDate = endDate,
                slashPriceStatusId = it.slashPriceStatusId
            )
        }
    }

    private fun mapToListProductVariant(
        productResponse: GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList.ProductList,
        selectedProductVariantId: String
    ): List<ShopDiscountSetupProductUiModel.SetupProductData> {
        return productResponse.variants.filter {
            if (selectedProductVariantId.isNotEmpty()) {
                it.productId == selectedProductVariantId
            } else {
                true
            }
        }.map {
            ShopDiscountSetupProductUiModel.SetupProductData(
                productId = it.productId,
                productName = it.name,
                productImageUrl = it.picture,
                stock = it.stock,
                listProductWarehouse = mapToListProductWarehouseData(it.warehouses),
                slashPriceInfo = mapToSlashPriceInfo(it.slashPriceInfo),
                price = mapToPriceData(it.price)
            )
        }
    }

    private fun mapToListProductWarehouseData(
        listWarehouseProductData: List<GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList.ProductList.Warehouses>
    ): List<ShopDiscountSetupProductUiModel.SetupProductData.ProductWarehouse> {
        return listWarehouseProductData.map {
            ShopDiscountSetupProductUiModel.SetupProductData.ProductWarehouse(
                warehouseId = it.warehouseId,
                warehouseName = it.warehouseName,
                warehouseLocation = it.warehouseLocation,
                warehouseStock = it.warehouseStock,
                maxOrder = it.maxOrder,
                abusiveRule = it.abusiveRule,
                avgSoldPrice = it.avgSoldPrice.toInt(),
                cheapestPrice = it.cheapestPrice.toInt(),
                discountedPrice = it.discountedPrice.toInt(),
                discountedPercentage = it.discountedPercentage,
                minRecommendationPrice = it.minRecommendationPrice.toInt(),
                minRecommendationPercentage = it.minRecommendationPercentage,
                maxRecommendationPrice = it.maxRecommendationPrice.toInt(),
                maxRecommendationPercentage = it.maxRecommendationPercentage,
                disable = it.disable,
                disableRecommendation = it.disableRecommendation,
                warehouseType = it.warehouseType,
                originalPrice = it.originalPrice.toInt()
            )
        }
    }

    fun mapToDoSlashPriceSubmissionRequest(
        listProductData: List<ShopDiscountSetupProductUiModel.SetupProductData>,
        action: String
    ): DoSlashPriceProductSubmissionRequest {
        return DoSlashPriceProductSubmissionRequest(
            requestHeader = getRequestHeader(),
            action = action,
            listSubmittedSlashPriceProduct = mapToListSubmittedSlashPriceProduct(listProductData)
        )
    }

    private fun mapToListSubmittedSlashPriceProduct(
        listProductData: List<ShopDiscountSetupProductUiModel.SetupProductData>
    ): List<DoSlashPriceProductSubmissionRequest.SubmittedSlashPriceProduct> {
        val listRequest: MutableList<DoSlashPriceProductSubmissionRequest.SubmittedSlashPriceProduct> =
            mutableListOf()
        listProductData.filter {
            it.listProductWarehouse.none { listProdWarehouse ->
                listProdWarehouse.abusiveRule
            }
        }.forEach { setupProductData ->
            if (setupProductData.productStatus.isVariant) {
                val listProductVariantRequest =
                    setupProductData.listProductVariant.filter {
                        it.variantStatus.isVariantEnabled == true
                    }.map { setupProductVariantData ->
                        mapToDoSlashPriceProductSubmissionRequest(setupProductVariantData)
                    }
                listRequest.addAll(listProductVariantRequest)
            } else {
                val productRequest = mapToDoSlashPriceProductSubmissionRequest(setupProductData)
                listRequest.add(productRequest)
            }
        }
        return listRequest
    }

    private fun mapToDoSlashPriceProductSubmissionRequest(
        it: ShopDiscountSetupProductUiModel.SetupProductData
    ): DoSlashPriceProductSubmissionRequest.SubmittedSlashPriceProduct {
        return DoSlashPriceProductSubmissionRequest.SubmittedSlashPriceProduct(
            productId = it.productId,
            startTimeUnix = (it.slashPriceInfo.startDate.time / 1000L).toString(),
            endTimeUnix = (it.slashPriceInfo.endDate.time / 1000L).toString(),
            slashPriceWarehouses = mapToListSlashPriceWarehouse(it.listProductWarehouse, it.stock)
        )
    }

    private fun mapToListSlashPriceWarehouse(
        listProductWarehouse: List<ShopDiscountSetupProductUiModel.SetupProductData.ProductWarehouse>,
        stock: String
    ): List<DoSlashPriceProductSubmissionRequest.SubmittedSlashPriceProduct.SlashPriceWarehouse> {
        return listProductWarehouse.map {
            DoSlashPriceProductSubmissionRequest.SubmittedSlashPriceProduct.SlashPriceWarehouse(
                key = it.warehouseId,
                value = mapToWarehouseValue(it, stock)
            )
        }
    }

    private fun mapToWarehouseValue(
        productWarehouse: ShopDiscountSetupProductUiModel.SetupProductData.ProductWarehouse,
        stock: String
    ): DoSlashPriceProductSubmissionRequest.SubmittedSlashPriceProduct.SlashPriceWarehouse.Value {
        val maxOrder = productWarehouse.maxOrder.takeIf {
            !it.toIntOrZero().isZero()
        } ?: stock
        return DoSlashPriceProductSubmissionRequest.SubmittedSlashPriceProduct.SlashPriceWarehouse.Value(
            warehouseId = productWarehouse.warehouseId,
            maxOrder = maxOrder,
            discountedPrice = productWarehouse.discountedPrice.toDouble(),
            discountedPercentage = productWarehouse.discountedPercentage,
            enable = !productWarehouse.disable
        )
    }

    fun mapToSubmitSlashPriceUiModel(response: DoSlashPriceProductSubmissionResponse): ShopDiscountSlashPriceProductSubmissionUiModel {
        return ShopDiscountSlashPriceProductSubmissionUiModel(
            responseHeader = response.doSlashPriceProductSubmission.responseHeader,
            listSubmittedProductData = mapToListSubmittedProductData(response.doSlashPriceProductSubmission.data)
        )
    }

    private fun mapToListSubmittedProductData(data: List<DoSlashPriceProductSubmissionResponse.DoSlashPriceProductSubmission.Data>): List<ShopDiscountSlashPriceProductSubmissionUiModel.SubmittedProductData> {
        return data.map {
            ShopDiscountSlashPriceProductSubmissionUiModel.SubmittedProductData(
                name = it.name,
                success = it.success,
                message = it.message,
                listSubmittedWarehouse = mapToListSubmittedWarehouse(it.warehouses)
            )
        }
    }

    private fun mapToListSubmittedWarehouse(listWarehouse: ArrayList<DoSlashPriceProductSubmissionResponse.DoSlashPriceProductSubmission.Data.Warehouses>): List<ShopDiscountSlashPriceProductSubmissionUiModel.SubmittedProductData.WarehouseData> {
        return listWarehouse.map {
            ShopDiscountSlashPriceProductSubmissionUiModel.SubmittedProductData.WarehouseData(
                warehouseId = it.value.warehouseId,
                success = it.value.success,
                message = it.value.message
            )
        }
    }

    fun mapToDoSlashPriceStopRequest(
        productId: String,
        position: String,
        requestId: String,
        state: String
    ): DoSlashPriceReservationRequest {
        return DoSlashPriceReservationRequest(
            requestHeader = getRequestHeader(),
            action = DoSlashPriceReservationRequest.DoSlashPriceReservationAction.DELETE,
            requestId = requestId,
            state = state,
            listProductData = listOf(
                DoSlashPriceReservationRequest.SlashPriceReservationProduct(
                    productId = productId,
                    position = position
                )
            )
        )
    }

    fun mapToShopDiscountSlashPriceStopUiModel(
        response: DoSlashPriceProductReservationResponse,
        productId: String
    ): ShopDiscountSlashPriceStopUiModel {
        return ShopDiscountSlashPriceStopUiModel(
            responseHeader = response.doSlashPriceProductReservation.responseHeader,
            productId = productId
        )
    }
}
