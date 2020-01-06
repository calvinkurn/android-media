package com.tokopedia.product.detail.usecase

import com.tokopedia.common_tradein.model.TradeInParams
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.view.debugTrace
import com.tokopedia.product.detail.common.ProductDetailCommonConstant
import com.tokopedia.product.detail.common.data.model.warehouse.MultiOriginWarehouse
import com.tokopedia.product.detail.data.model.ProductInfoP2ShopData
import com.tokopedia.product.detail.data.model.TradeinResponse
import com.tokopedia.product.detail.di.RawQueryKeyConstant
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopCodStatus
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetProductInfoP2ShopUseCase @Inject constructor(private val rawQueries: Map<String, String>,
                                                      private val graphqlRepository: GraphqlRepository) : UseCase<ProductInfoP2ShopData>() {

    companion object {
        fun createParams(shopId: Int, productId: String, warehouseId: String, forceRefresh: Boolean,
                         tradeinParams: TradeInParams): RequestParams {
            val requestParams = RequestParams()
            requestParams.putInt(ProductDetailCommonConstant.PARAM_SHOP_IDS, shopId)
            requestParams.putString(ProductDetailCommonConstant.PARAM_PRODUCT_ID, productId)
            requestParams.putString(ProductDetailCommonConstant.PARAM_WAREHOUSE_ID, warehouseId)
            requestParams.putBoolean(ProductDetailCommonConstant.FORCE_REFRESH, forceRefresh)
            requestParams.putObject(ProductDetailCommonConstant.PARAM_TRADE_IN, tradeinParams)

            return requestParams
        }
    }

    var requestParams: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): ProductInfoP2ShopData {
        val p2Shop = ProductInfoP2ShopData()
        val shopId = requestParams.getInt(ProductDetailCommonConstant.PARAM_SHOP_IDS, 0)
        val productId = requestParams.getString(ProductDetailCommonConstant.PARAM_PRODUCT_ID, "")
        val warehouseId = requestParams.getString(ProductDetailCommonConstant.PARAM_WAREHOUSE_ID, "")
        val forceRefresh = requestParams.getBoolean(ProductDetailCommonConstant.FORCE_REFRESH, false)
        val tradeInParams: TradeInParams = requestParams.getObject(ProductDetailCommonConstant.PARAM_TRADE_IN) as TradeInParams

        val shopParams = mapOf(ProductDetailCommonConstant.PARAM_SHOP_IDS to listOf(shopId),
                ProductDetailCommonConstant.PARAM_SHOP_FIELDS to ProductDetailCommonConstant.DEFAULT_SHOP_FIELDS)
        val shopRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_SHOP], ShopInfo.Response::class.java, shopParams)

        val nearestWarehouseParam = mapOf(
                "productIds" to listOf(productId),
                "warehouseID" to warehouseId
        )
        val nearestWarehouseRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_MULTI_ORIGIN],
                MultiOriginWarehouse.Response::class.java, nearestWarehouseParam)

        val shopCodParam = mapOf(ProductDetailCommonConstant.PARAM_SHOP_ID to shopId.toString())
        val shopCodRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_SHOP_COD_STATUS],
                ShopCodStatus.Response::class.java, shopCodParam)

        /*
         * Since GraphqlRepository doesn't support caching Pojo parameter,
         * it has to create map instead of passing object itself
         */
        val tradeinRequestMap = mapOf("CategoryId" to tradeInParams.categoryId,
                "DeviceId" to tradeInParams.deviceId,
                "isEligible" to tradeInParams.isEligible,
                "IsOnCampaign" to tradeInParams.isOnCampaign,
                "IsPreOrder" to tradeInParams.isPreorder,
                "ModelId" to tradeInParams.modelID,
                "NewPrice" to tradeInParams.newPrice,
                "ProductId" to tradeInParams.productId,
                "productName" to tradeInParams.productName,
                "remainingPrice" to tradeInParams.remainingPrice,
                "ShopId" to tradeInParams.shopId,
                "TradeInType" to tradeInParams.tradeInType,
                "useKyc" to tradeInParams.isUseKyc,
                "usedPrice" to tradeInParams.usedPrice,
                "UserId" to tradeInParams.userId)
        val pdpTradeinParam = mapOf(ProductDetailCommonConstant.PARAMS to tradeinRequestMap)
        val pdpTradeinRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_TRADE_IN],
                TradeinResponse::class.java, pdpTradeinParam)

        val cacheStrategy = GraphqlCacheStrategy.Builder(if (forceRefresh) CacheType.ALWAYS_CLOUD else CacheType.CACHE_FIRST).build()
        val requests = mutableListOf(shopRequest, shopCodRequest, nearestWarehouseRequest, pdpTradeinRequest)

        try {
            val gqlResponse = graphqlRepository.getReseponse(requests, cacheStrategy)

            if (gqlResponse.getError(ShopInfo.Response::class.java)?.isNotEmpty() != true) {
                val result = gqlResponse.getData<ShopInfo.Response>(ShopInfo.Response::class.java)
                if (result.result.data.isNotEmpty())
                    p2Shop.shopInfo = result.result.data.first()
            }

            if (gqlResponse.getError(ShopCodStatus.Response::class.java)?.isNotEmpty() != true) {
                p2Shop.shopCod = gqlResponse.getData<ShopCodStatus.Response>(ShopCodStatus.Response::class.java)
                        .result.shopCodStatus.isCod
            }

            if (gqlResponse.getError(MultiOriginWarehouse.Response::class.java)?.isNotEmpty() != true) {
                gqlResponse.getData<MultiOriginWarehouse.Response>(MultiOriginWarehouse.Response::class.java)
                        .result.data.firstOrNull()?.let { p2Shop.nearestWarehouse = it }
            }

            if (gqlResponse.getError(TradeinResponse::class.java)?.isNotEmpty() != true) {
                val tradeinResponse: TradeinResponse = gqlResponse.getData(TradeinResponse::class.java)
                p2Shop.tradeinResponse = tradeinResponse
            }

        } catch (t: Throwable) {
            t.debugTrace()
        }

        return p2Shop
    }

}