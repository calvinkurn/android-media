package com.tokopedia.shopdiscount.manage_discount.util

import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.shopdiscount.common.data.request.RequestHeader
import com.tokopedia.shopdiscount.manage_discount.data.request.DoSlashPriceProductSubmissionRequest
import com.tokopedia.shopdiscount.manage_discount.data.request.DoSlashPriceReservationRequest
import com.tokopedia.shopdiscount.manage_discount.data.request.GetSlashPriceSetupProductListRequest
import com.tokopedia.shopdiscount.manage_discount.data.response.DoSlashPriceProductSubmissionResponse
import com.tokopedia.shopdiscount.manage_discount.data.response.DoSlashPriceProductReservationResponse
import com.tokopedia.shopdiscount.manage_discount.data.response.GetSlashPriceSetupProductListResponse
import com.tokopedia.shopdiscount.manage_discount.data.uimodel.ShopDiscountSetupProductUiModel
import com.tokopedia.shopdiscount.manage_discount.data.uimodel.ShopDiscountSlashPriceProductSubmissionUiModel
import com.tokopedia.shopdiscount.manage_discount.data.uimodel.ShopDiscountSlashPriceStopUiModel
import com.tokopedia.shopdiscount.utils.constant.DateConstant
import com.tokopedia.utils.date.toDate

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
        response: GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList
    ): ShopDiscountSetupProductUiModel {
        return ShopDiscountSetupProductUiModel(
            responseHeader = response.responseHeader,
            listSetupProductData = mapToListSetupProductUiModel(response.productList)
        )
    }

    private fun getRequestHeader(): RequestHeader {
        return RequestHeader(
            source = "",
            ip = "",
            usecase = ""
        )
    }

    private fun mapToListSetupProductUiModel(
        listProductResponse: List<GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList.ProductList>
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
                listProductVariant = mapToListProductVariant(it)
            ).apply {
                updateProductStatusAndMappedData(this)
            }
        }
    }

    fun updateProductStatusAndMappedData(uiModel: ShopDiscountSetupProductUiModel.SetupProductData) {
        uiModel.productStatus = mapToProductStatus(uiModel)
        uiModel.mappedResultData = mapToMappedResultData(uiModel)
    }

    private fun mapToProductStatus(
        setupProductUiModel: ShopDiscountSetupProductUiModel.SetupProductData
    ): ShopDiscountSetupProductUiModel.SetupProductData.ProductStatus {
        return ShopDiscountSetupProductUiModel.SetupProductData.ProductStatus(
            isProductDiscounted = getIsProductDiscounted(setupProductUiModel),
            isVariant = isVariant(setupProductUiModel),
            isMultiLoc = isMultiLoc(setupProductUiModel),
        )
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
            ShopDiscountSetupProductUiModel.SetupProductData.ProductSlashPriceInfo(
                slashPriceProductId = it.slashPriceProductId,
                discountedPrice = it.discountedPrice,
                discountPercentage = it.discountPercentage,
                startDate = it.startDate.toDate(DateConstant.DATE_TIME_SECOND_PRECISION_WITH_TIMEZONE),
                endDate = it.endDate.toDate(DateConstant.DATE_TIME_SECOND_PRECISION_WITH_TIMEZONE),
                slashPriceStatusId = it.slashPriceStatusId
            )
        }
    }

    private fun mapToListProductVariant(
        productResponse: GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList.ProductList
    ): List<ShopDiscountSetupProductUiModel.SetupProductData> {
        return productResponse.variants.map {
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
                avgSoldPrice = it.avgSoldPrice,
                cheapestPrice = it.cheapestPrice,
                discountedPrice = it.discountedPrice,
                discountedPercentage = it.discountedPercentage,
                minRecommendationPrice = it.minRecommendationPrice,
                minRecommendationPercentage = it.minRecommendationPercentage,
                maxRecommendationPrice = it.maxRecommendationPrice,
                maxRecommendationPercentage = it.maxRecommendationPercentage,
                disable = it.disable,
                disableRecommendation = it.disableRecommendation,
                warehouseType = it.warehouseType,
                originalPrice = it.originalPrice
            )
        }
    }

    private fun mapToMappedResultData(
        setupProductUiModel: ShopDiscountSetupProductUiModel.SetupProductData
    ): ShopDiscountSetupProductUiModel.SetupProductData.MappedResultData {
        return ShopDiscountSetupProductUiModel.SetupProductData.MappedResultData(
            minOriginalPrice = getMinOriginalPrice(setupProductUiModel),
            maxOriginalPrice = getMaxOriginalPrice(setupProductUiModel),
            minDisplayedPrice = getMinDisplayedPrice(setupProductUiModel),
            maxDisplayedPrice = getMaxDisplayedPrice(setupProductUiModel),
            minDiscountPercentage = getMinDiscountPercentage(setupProductUiModel),
            maxDiscountPercentage = getMaxDiscountPercentage(setupProductUiModel),
            totalVariant = getTotalVariant(setupProductUiModel),
            totalDiscountedVariant = getTotalDiscountedVariant(setupProductUiModel),
            totalLocation = getTotalLocation(setupProductUiModel)
        )
    }

    private fun getTotalLocation(
        setupProductUiModel: ShopDiscountSetupProductUiModel.SetupProductData
    ): Int {
        return setupProductUiModel.listProductWarehouse.size
    }

    private fun getTotalDiscountedVariant(
        setupProductDataUiModel: ShopDiscountSetupProductUiModel.SetupProductData
    ): Int {
        return setupProductDataUiModel.listProductVariant.count {
            it.listProductWarehouse.any { productWarehouse ->
                !productWarehouse.discountedPercentage.isZero()
            }
        }
    }

    private fun getTotalVariant(
        setupProductDataUiModel: ShopDiscountSetupProductUiModel.SetupProductData
    ): Int {
        return setupProductDataUiModel.listProductVariant.size
    }

    private fun getIsProductDiscounted(
        setupProductDataUiModel: ShopDiscountSetupProductUiModel.SetupProductData
    ): Boolean {
        return if (isVariant(setupProductDataUiModel)) {
            setupProductDataUiModel.listProductVariant.any {
                it.listProductWarehouse.any { productWarehouse ->
                    !productWarehouse.discountedPercentage.isZero()
                }
            }
        } else {
            setupProductDataUiModel.listProductWarehouse.any {
                !it.discountedPercentage.isZero()
            }
        }
    }

    private fun isMultiLoc(
        setupProductDataUiModel: ShopDiscountSetupProductUiModel.SetupProductData
    ): Boolean {
        return if (isVariant(setupProductDataUiModel)) {
            setupProductDataUiModel.listProductVariant.any {
                it.listProductWarehouse.size > 1
            }
        } else {
            setupProductDataUiModel.listProductWarehouse.size > 1
        }
    }

    private fun getMaxOriginalPrice(
        setupProductDataUiModel: ShopDiscountSetupProductUiModel.SetupProductData
    ): Int {
        return if (isVariant(setupProductDataUiModel)) {
            setupProductDataUiModel.listProductVariant.map {
                it.listProductWarehouse.map { productWarehouse ->
                    productWarehouse.originalPrice
                }.maxOrNull().orZero()
            }.maxOrNull().orZero()
        } else {
            setupProductDataUiModel.listProductWarehouse.map {
                it.originalPrice
            }.maxOrNull().orZero()
        }
    }

    private fun getMinOriginalPrice(
        setupProductDataUiModel: ShopDiscountSetupProductUiModel.SetupProductData
    ): Int {
        return if (isVariant(setupProductDataUiModel)) {
            setupProductDataUiModel.listProductVariant.map {
                it.listProductWarehouse.map { productWarehouse ->
                    productWarehouse.originalPrice
                }.minOrNull().orZero()
            }.minOrNull().orZero()
        } else {
            setupProductDataUiModel.listProductWarehouse.map {
                it.originalPrice
            }.minOrNull().orZero()
        }
    }

    private fun getMaxDisplayedPrice(
        setupProductDataUiModel: ShopDiscountSetupProductUiModel.SetupProductData
    ): Int {
        return if (isVariant(setupProductDataUiModel)) {
            setupProductDataUiModel.listProductVariant.map {
                it.listProductWarehouse.map { productWarehouse ->
                    productWarehouse.discountedPrice
                }.maxOrNull().orZero()
            }.maxOrNull().orZero()
        } else {
            setupProductDataUiModel.listProductWarehouse.map {
                it.discountedPrice
            }.maxOrNull().orZero()
        }
    }

    private fun getMinDisplayedPrice(
        setupProductDataUiModel: ShopDiscountSetupProductUiModel.SetupProductData
    ): Int {
        return if (isVariant(setupProductDataUiModel)) {
            setupProductDataUiModel.listProductVariant.map {
                it.listProductWarehouse.map { productWarehouse ->
                    productWarehouse.discountedPrice
                }.minOrNull().orZero()
            }.minOrNull().orZero()
        } else {
            setupProductDataUiModel.listProductWarehouse.map {
                it.discountedPrice
            }.minOrNull().orZero()
        }
    }

    private fun getMaxDiscountPercentage(
        setupProductDataUiModel: ShopDiscountSetupProductUiModel.SetupProductData
    ): Int {
        return if (isVariant(setupProductDataUiModel)) {
            setupProductDataUiModel.listProductVariant.map {
                it.listProductWarehouse.map { productWarehouse ->
                    productWarehouse.discountedPercentage
                }.maxOrNull().orZero()
            }.maxOrNull().orZero()
        } else {
            setupProductDataUiModel.listProductWarehouse.map {
                it.discountedPercentage
            }.maxOrNull().orZero()
        }
    }

    private fun getMinDiscountPercentage(
        setupProductDataUiModel: ShopDiscountSetupProductUiModel.SetupProductData
    ): Int {
        return if (isVariant(setupProductDataUiModel)) {
            setupProductDataUiModel.listProductVariant.map {
                it.listProductWarehouse.map { productWarehouse ->
                    productWarehouse.discountedPercentage
                }.minOrNull().orZero()
            }.minOrNull().orZero()
        } else {
            setupProductDataUiModel.listProductWarehouse.map {
                it.discountedPercentage
            }.minOrNull().orZero()
        }
    }

    private fun isVariant(setupProductUiModel: ShopDiscountSetupProductUiModel.SetupProductData): Boolean {
        return setupProductUiModel.listProductVariant.isNotEmpty()
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
        listProductData.forEach { setupProductData ->
            if (setupProductData.productStatus.isVariant) {
                val listProductVariantRequest =
                    setupProductData.listProductVariant.map { setupProductVariantData ->
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
            startTimeUnix = (it.slashPriceInfo.startDate.time/1000L).toString(),
            endTimeUnix = (it.slashPriceInfo.endDate.time/1000L).toString(),
            slashPriceWarehouses = mapToListSlashPriceWarehouse(it.listProductWarehouse)
        )
    }

    private fun mapToListSlashPriceWarehouse(
        listProductWarehouse: List<ShopDiscountSetupProductUiModel.SetupProductData.ProductWarehouse>
    ): List<DoSlashPriceProductSubmissionRequest.SubmittedSlashPriceProduct.SlashPriceWarehouse> {
        return listProductWarehouse.map {
            DoSlashPriceProductSubmissionRequest.SubmittedSlashPriceProduct.SlashPriceWarehouse(
                key = it.warehouseId,
                value = mapToWarehouseValue(it)
            )
        }
    }

    private fun mapToWarehouseValue(
        productWarehouse: ShopDiscountSetupProductUiModel.SetupProductData.ProductWarehouse
    ): DoSlashPriceProductSubmissionRequest.SubmittedSlashPriceProduct.SlashPriceWarehouse.Value {
        return DoSlashPriceProductSubmissionRequest.SubmittedSlashPriceProduct.SlashPriceWarehouse.Value(
            warehouseId = productWarehouse.warehouseId,
            maxOrder = productWarehouse.maxOrder,
            discountedPrice = productWarehouse.discountedPrice,
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
                success = it.success,
                message = it.message
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
            action =  DoSlashPriceReservationRequest.DoSlashPriceReservationAction.DELETE,
            requestId = requestId,
            state = state,
            listProductData = listOf(DoSlashPriceReservationRequest.SlashPriceReservationProduct(
                productId = productId,
                position = position,
            ))
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