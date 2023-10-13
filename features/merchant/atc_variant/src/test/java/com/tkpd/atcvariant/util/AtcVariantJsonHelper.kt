package com.tkpd.atcvariant.util

import com.tokopedia.graphql.CommonUtils
import com.tokopedia.minicart.common.data.response.minicartlist.MiniCartGqlResponse
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.data.mapProductsWithProductId
import com.tokopedia.minicart.common.domain.mapper.MiniCartSimplifiedMapper
import com.tokopedia.product.detail.common.data.model.aggregator.AggregatorMiniCartUiModel
import com.tokopedia.product.detail.common.data.model.aggregator.ProductVariantAggregator
import com.tokopedia.product.detail.common.data.model.aggregator.ProductVariantAggregatorResponse
import com.tokopedia.product.detail.common.data.model.aggregator.ProductVariantAggregatorUiData
import com.tokopedia.product.detail.common.data.model.aggregator.ProductVariantBottomSheetParams
import java.io.File
import java.lang.reflect.Type

/**
 * Created by Yehezkiel on 28/05/21
 */
object AtcVariantJsonHelper {

    const val GQL_VARIANT_AGGREGATOR_RESPONSE_JSON = "json/variant_aggregator_level_2.json"
    const val GQL_MINI_CART_RESPONSE_JSON = "json/mini_cart_response.json"

    fun generateAggregatorData(isTokoNow: Boolean): AggregatorMiniCartUiModel {
        val mockResponse = mockAggregatorMiniCart()

        return AggregatorMiniCartUiModel(
            mockResponse.first,
            if (!isTokoNow) null else mockResponse.second.miniCartItems
        )
    }

    fun generateParamsVariantFulfilled(
        productId: String,
        isTokoNow: Boolean,
        emptyMiniCart: Boolean = false
    ): ProductVariantBottomSheetParams {
        val mockResponse = mockAggregatorMiniCart()

        return ProductVariantBottomSheetParams(
            productId = productId,
            pageSource = "product detail page",
            isTokoNow = isTokoNow,
            variantAggregator = mockResponse.first,
            showQtyEditor = isTokoNow,
            miniCartData = if (!isTokoNow || emptyMiniCart) null else mockResponse.second.miniCartItems.mapProductsWithProductId()
        )
    }

    fun generateParamsVariantFulfilledWithMiniCartData(
        productId: String,
        isTokoNow: Boolean,
    ): ProductVariantBottomSheetParams {
        val mockResponse = mockAggregatorMiniCart()

        return ProductVariantBottomSheetParams(
            productId = productId,
            pageSource = "product detail page",
            isTokoNow = isTokoNow,
            variantAggregator = mockResponse.first,
            showQtyEditor = isTokoNow,
            miniCartData = mockResponse.second.miniCartItems.mapProductsWithProductId()
        )
    }

    fun generateParamsVariant(
        productId: String,
        isTokoNow: Boolean,
        showQtyEditor: Boolean
    ): ProductVariantBottomSheetParams {
        return ProductVariantBottomSheetParams(
            productId = productId,
            pageSource = "wishlist",
            isTokoNow = isTokoNow,
            showQtyEditor = showQtyEditor
        )
    }

    private fun mockAggregatorMiniCart(): Pair<ProductVariantAggregatorUiData, MiniCartSimplifiedData> {
        val variantAggregatorResponse: ProductVariantAggregatorResponse =
            createMockGraphqlSuccessResponse(
                GQL_VARIANT_AGGREGATOR_RESPONSE_JSON,
                ProductVariantAggregatorResponse::class.java
            )
        val miniCartResponse: MiniCartGqlResponse = createMockGraphqlSuccessResponse(
            GQL_MINI_CART_RESPONSE_JSON,
            MiniCartGqlResponse::class.java
        )

        val variantAggregator = mapVariantAggregator(variantAggregatorResponse.response)
        val miniCart =
            MiniCartSimplifiedMapper().mapMiniCartSimplifiedData(miniCartResponse.miniCart)

        return variantAggregator to miniCart
    }

    private fun <T> createMockGraphqlSuccessResponse(jsonLocation: String, typeOfClass: Type): T {
        return CommonUtils.fromJson(
            getJsonFromFile(jsonLocation),
            typeOfClass
        ) as T
    }

    private fun mapVariantAggregator(data: ProductVariantAggregator): ProductVariantAggregatorUiData {
        return ProductVariantAggregatorUiData(
            data.variantData,
            data.cardRedirection.data.associateBy({ it.productId }, { it }),
            data.nearestWarehouse.associateBy({ it.productId }, { it.warehouseInfo }),
            data.cardRedirection.alternateCopy,
            data.ratesEstimate,
            data.restrictionInfo,
            data.uniqueSellingPoint.uspBoe.uspIcon,
            data.isCashback.percentage
        )
    }

    private fun getJsonFromFile(path: String): String {
        val uri = ClassLoader.getSystemClassLoader().getResource(path)
        val file = File(uri.path)
        return String(file.readBytes())
    }

}