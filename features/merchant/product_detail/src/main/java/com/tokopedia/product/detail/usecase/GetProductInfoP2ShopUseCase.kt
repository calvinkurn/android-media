package com.tokopedia.product.detail.usecase

import com.tokopedia.common_tradein.model.TradeInParams
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.product.detail.common.ProductDetailCommonConstant
import com.tokopedia.product.detail.common.data.model.warehouse.MultiOriginWarehouse
import com.tokopedia.product.detail.data.model.ProductInfoP2ShopData
import com.tokopedia.product.detail.data.model.TradeinResponse
import com.tokopedia.product.detail.di.RawQueryKeyConstant
import com.tokopedia.product.detail.view.util.CacheStrategyUtil
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import timber.log.Timber
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

        /*
         * Since GraphqlRepository doesn't support caching Pojo parameter,
         * it has to create map instead of passing object itself
         */
        val tradeinRequestMap = mapOf(
                ProductDetailCommonConstant.PARAM_TRADEIN_CATEGORY_ID to tradeInParams.categoryId,
                ProductDetailCommonConstant.PARAM_TRADEIN_DEVICE_ID to tradeInParams.deviceId,
                ProductDetailCommonConstant.PARAM_TRADEIN_IS_ELIGIBLE to tradeInParams.isEligible,
                ProductDetailCommonConstant.PARAM_TRADEIN_IS_ON_CAMPAIGN to tradeInParams.isOnCampaign,
                ProductDetailCommonConstant.PARAM_TRADEIN_IS_PRE_ORDER to tradeInParams.isPreorder,
                ProductDetailCommonConstant.PARAM_TRADEIN_MODEL_ID to tradeInParams.modelID,
                ProductDetailCommonConstant.PARAM_TRADEIN_NEW_PRICE to tradeInParams.newPrice,
                ProductDetailCommonConstant.PARAM_TRADEIN_PRODUCT_ID to tradeInParams.productId,
                ProductDetailCommonConstant.PARAM_TRADEIN_PRODUCT_NAME to tradeInParams.productName,
                ProductDetailCommonConstant.PARAM_TRADEIN_REMAINING_PRICE to tradeInParams.remainingPrice,
                ProductDetailCommonConstant.PARAM_TRADEIN_SHOP_ID to tradeInParams.shopId,
                ProductDetailCommonConstant.PARAM_TRADEIN_TRADE_IN_TYPE to tradeInParams.tradeInType,
                ProductDetailCommonConstant.PARAM_TRADEIN_TRADE_USE_KYC to tradeInParams.isUseKyc,
                ProductDetailCommonConstant.PARAM_TRADEIN_TRADE_USED_PRICE to tradeInParams.usedPrice,
                ProductDetailCommonConstant.PARAM_TRADEIN_TRADE_USER_ID to tradeInParams.userId)

        val pdpTradeinParam = mapOf(ProductDetailCommonConstant.PARAMS to tradeinRequestMap)
        val pdpTradeinRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_TRADE_IN],
                TradeinResponse::class.java, pdpTradeinParam)

        val requests = mutableListOf(shopRequest, nearestWarehouseRequest, pdpTradeinRequest)

        try {
            val gqlResponse = graphqlRepository.getReseponse(requests, CacheStrategyUtil.getCacheStrategy(forceRefresh))

            if (gqlResponse.getError(ShopInfo.Response::class.java)?.isNotEmpty() != true) {
                val result = gqlResponse.getData<ShopInfo.Response>(ShopInfo.Response::class.java)
                if (result.result.data.isNotEmpty())
                    p2Shop.shopInfo = result.result.data.first()
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
            Timber.d(t)
        }
        return p2Shop
    }
}