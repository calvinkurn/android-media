package com.tokopedia.product.detail.usecase

import com.tokopedia.common_tradein.model.TradeInParams
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.product.detail.common.ProductDetailCommonConstant
import com.tokopedia.product.detail.common.data.model.carttype.CartRedirectionParams
import com.tokopedia.product.detail.common.data.model.carttype.CartRedirectionResponse
import com.tokopedia.product.detail.data.model.ProductInfoP2ShopData
import com.tokopedia.product.detail.data.model.TradeinResponse
import com.tokopedia.product.detail.data.util.DynamicProductDetailMapper
import com.tokopedia.product.detail.di.RawQueryKeyConstant
import com.tokopedia.product.detail.view.util.CacheStrategyUtil
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopRatingStats
import com.tokopedia.shop.common.graphql.data.shopspeed.ProductShopChatSpeed
import com.tokopedia.shop.common.graphql.data.shopspeed.ProductShopSpeed
import com.tokopedia.stickylogin.data.StickyLoginTickerPojo
import com.tokopedia.stickylogin.internal.StickyLoginConstant
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.variant_common.model.VariantMultiOriginResponse
import com.tokopedia.variant_common.model.VariantMultiOriginWarehouse
import timber.log.Timber
import javax.inject.Inject

class GetProductInfoP2ShopUseCase @Inject constructor(private val rawQueries: Map<String, String>,
                                                      private val graphqlRepository: GraphqlRepository) : UseCase<ProductInfoP2ShopData>() {

    companion object {
        fun createParams(shopId: Int, productId: String, forceRefresh: Boolean, tradeinParams: TradeInParams,
                         cartTypeParam: List<CartRedirectionParams>, warehouseId: String?, shopCredibilityExist: Boolean): RequestParams {
            val requestParams = RequestParams()
            requestParams.putInt(ProductDetailCommonConstant.PARAM_SHOP_IDS, shopId)
            requestParams.putString(ProductDetailCommonConstant.PARAM_PRODUCT_ID, productId)
            requestParams.putBoolean(ProductDetailCommonConstant.FORCE_REFRESH, forceRefresh)
            requestParams.putObject(ProductDetailCommonConstant.PARAM_TRADE_IN, tradeinParams)
            requestParams.putObject(ProductDetailCommonConstant.PARAM_CART_TYPE, cartTypeParam)
            requestParams.putString(ProductDetailCommonConstant.PARAM_WAREHOUSE_ID, warehouseId)
            requestParams.putBoolean(ProductDetailCommonConstant.PARAM_SHOP_CREDIBILITY_EXIST, shopCredibilityExist)

            return requestParams
        }
    }

    var requestParams: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): ProductInfoP2ShopData {
        val p2Shop = ProductInfoP2ShopData()
        val shopId = requestParams.getInt(ProductDetailCommonConstant.PARAM_SHOP_IDS, 0)
        val productId = requestParams.getString(ProductDetailCommonConstant.PARAM_PRODUCT_ID, "")
        val forceRefresh = requestParams.getBoolean(ProductDetailCommonConstant.FORCE_REFRESH, false)
        val tradeInParams: TradeInParams = requestParams.getObject(ProductDetailCommonConstant.PARAM_TRADE_IN) as TradeInParams
        val cartTypeParam = requestParams.getObject(ProductDetailCommonConstant.PARAM_CART_TYPE)
        val warehouseId = requestParams.getString(ProductDetailCommonConstant.PARAM_WAREHOUSE_ID, null)
        val shopCredibilityExist = requestParams.getBoolean(ProductDetailCommonConstant.PARAM_SHOP_CREDIBILITY_EXIST, false)

        val getCartTypeParams = mapOf(ProductDetailCommonConstant.PARAMS to cartTypeParam)
        val getCartTypeRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_GET_CART_TYPE],
                CartRedirectionResponse::class.java, getCartTypeParams)

        val warehouseParam = mapOf(ProductDetailCommonConstant.PARAM_PRODUCT_IDS to productId,
                ProductDetailCommonConstant.PARAM_WAREHOUSE_ID to warehouseId)
        val warehouseRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_MULTI_ORIGIN],
                VariantMultiOriginResponse::class.java, warehouseParam)

        val shopParams = mapOf(ProductDetailCommonConstant.PARAM_SHOP_IDS to listOf(shopId),
                ProductDetailCommonConstant.PARAM_SHOP_FIELDS to ProductDetailCommonConstant.DEFAULT_SHOP_FIELDS)
        val shopRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_SHOP], ShopInfo.Response::class.java, shopParams)

        val shopChatSpeedParams = mapOf(ProductDetailCommonConstant.SHOP_ID_PARAM to shopId,
                ProductDetailCommonConstant.PARAM_SHOP_FIELDS to ProductDetailCommonConstant.DEFAULT_SHOP_FIELDS)
        val shopChatSpeedRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_SHOP_CHAT_SPEED], ProductShopChatSpeed::class.java, shopChatSpeedParams)

        val shopSpeedParams = mapOf(ProductDetailCommonConstant.SHOP_ID_PARAM to shopId,
                ProductDetailCommonConstant.PARAM_SHOP_FIELDS to ProductDetailCommonConstant.DEFAULT_SHOP_FIELDS)
        val shopSpeedRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_SHOP_SPEED], ProductShopSpeed::class.java, shopSpeedParams)

        val shopRatingParams = mapOf(ProductDetailCommonConstant.SHOP_ID_PARAM to shopId,
                ProductDetailCommonConstant.PARAM_SHOP_FIELDS to ProductDetailCommonConstant.DEFAULT_SHOP_FIELDS)
        val shopRatingRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_SHOP_RATING], ShopRatingStats.Response::class.java, shopRatingParams)

        val tickerParams = mapOf(StickyLoginConstant.PARAMS_PAGE to StickyLoginConstant.Page.PDP.toString())
        val tickerRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_TICKER], StickyLoginTickerPojo.TickerResponse::class.java, tickerParams)

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

        val requests = mutableListOf(shopRequest, pdpTradeinRequest, getCartTypeRequest, warehouseRequest, tickerRequest)

        if (shopCredibilityExist) {
            requests.addAll(mutableListOf(shopSpeedRequest, shopChatSpeedRequest, shopRatingRequest))
        }

        try {
            val gqlResponse = graphqlRepository.getReseponse(requests, CacheStrategyUtil.getCacheStrategy(forceRefresh))

            if (gqlResponse.getError(VariantMultiOriginResponse::class.java)?.isNotEmpty() != true) {
                p2Shop.variantMultiOrigin = gqlResponse.getData<VariantMultiOriginResponse>(VariantMultiOriginResponse::class.java).result.data.firstOrNull()
                        ?: VariantMultiOriginWarehouse()
            }

            if (gqlResponse.getError(ShopInfo.Response::class.java)?.isNotEmpty() != true) {
                val result = gqlResponse.getData<ShopInfo.Response>(ShopInfo.Response::class.java)
                if (result.result.data.isNotEmpty())
                    p2Shop.shopInfo = result.result.data.first()
            }

            if (gqlResponse.getError(TradeinResponse::class.java)?.isNotEmpty() != true) {
                val tradeinResponse: TradeinResponse = gqlResponse.getData(TradeinResponse::class.java)
                p2Shop.tradeinResponse = tradeinResponse
            }

            if (gqlResponse.getError(CartRedirectionResponse::class.java)?.isNotEmpty() != true) {
                p2Shop.cartRedirectionResponse = gqlResponse.getData<CartRedirectionResponse>(CartRedirectionResponse::class.java)
            }

            if (gqlResponse.getError(StickyLoginTickerPojo.TickerResponse::class.java)?.isNotEmpty() != true) {
                val tickerData = gqlResponse.getData<StickyLoginTickerPojo.TickerResponse>(StickyLoginTickerPojo.TickerResponse::class.java)
                p2Shop.tickerInfo = DynamicProductDetailMapper.getTickerInfoData(tickerData)
            }

            if (gqlResponse.getError(ProductShopChatSpeed::class.java)?.isNotEmpty() != true) {
                val shopChatSpeed = gqlResponse.getData<ProductShopChatSpeed>(ProductShopChatSpeed::class.java)
                p2Shop.shopChatSpeed = shopChatSpeed.response.messageResponseTime
            }

            if (gqlResponse.getError(ProductShopSpeed::class.java)?.isNotEmpty() != true) {
                val shopSpeed = gqlResponse.getData<ProductShopSpeed>(ProductShopSpeed::class.java)
                p2Shop.shopSpeed = shopSpeed.response.hour
            }

            if (gqlResponse.getError(ShopRatingStats.Response::class.java)?.isNotEmpty() != true) {
                val shopRating = gqlResponse.getData<ShopRatingStats.Response>(ShopRatingStats.Response::class.java)
                p2Shop.shopRating = shopRating.shopRatingStats.ratingScore
            }

        } catch (t: Throwable) {
            Timber.d(t)
        }
        return p2Shop
    }
}