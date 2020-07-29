package com.tokopedia.product.detail.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.product.detail.common.ProductDetailCommonConstant
import com.tokopedia.product.detail.common.data.model.carttype.CartRedirectionParams
import com.tokopedia.product.detail.common.data.model.carttype.CartRedirectionResponse
import com.tokopedia.product.detail.data.model.ProductInfoP2ShopData
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
        fun createParams(shopId: Int, productId: String, forceRefresh: Boolean,
                         cartTypeParam: List<CartRedirectionParams>, warehouseId: String?): RequestParams {
            val requestParams = RequestParams()
            requestParams.putInt(ProductDetailCommonConstant.PARAM_SHOP_IDS, shopId)
            requestParams.putString(ProductDetailCommonConstant.PARAM_PRODUCT_ID, productId)
            requestParams.putBoolean(ProductDetailCommonConstant.FORCE_REFRESH, forceRefresh)
            requestParams.putObject(ProductDetailCommonConstant.PARAM_CART_TYPE, cartTypeParam)
            requestParams.putString(ProductDetailCommonConstant.PARAM_WAREHOUSE_ID, warehouseId)

            return requestParams
        }
    }

    var requestParams: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): ProductInfoP2ShopData {
        val p2Shop = ProductInfoP2ShopData()
        val shopId = requestParams.getInt(ProductDetailCommonConstant.PARAM_SHOP_IDS, 0)
        val forceRefresh = requestParams.getBoolean(ProductDetailCommonConstant.FORCE_REFRESH, false)
        val cartTypeParam = requestParams.getObject(ProductDetailCommonConstant.PARAM_CART_TYPE)

        val getCartTypeParams = mapOf(ProductDetailCommonConstant.PARAMS to cartTypeParam)
        val getCartTypeRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_GET_CART_TYPE],
                CartRedirectionResponse::class.java, getCartTypeParams)

        val shopParams = mapOf(ProductDetailCommonConstant.PARAM_SHOP_IDS to listOf(shopId),
                ProductDetailCommonConstant.PARAM_SHOP_FIELDS to ProductDetailCommonConstant.DEFAULT_SHOP_FIELDS)
        val shopRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_SHOP], ShopInfo.Response::class.java, shopParams)

        val requests = mutableListOf(shopRequest, getCartTypeRequest)

        try {
            val gqlResponse = graphqlRepository.getReseponse(requests, CacheStrategyUtil.getCacheStrategy(forceRefresh))

            if (gqlResponse.getError(ShopInfo.Response::class.java)?.isNotEmpty() != true) {
                val result = gqlResponse.getData<ShopInfo.Response>(ShopInfo.Response::class.java)
                if (result.result.data.isNotEmpty())
                    p2Shop.shopInfo = result.result.data.first()
            }

            if (gqlResponse.getError(CartRedirectionResponse::class.java)?.isNotEmpty() != true) {
                p2Shop.cartRedirectionResponse = gqlResponse.getData<CartRedirectionResponse>(CartRedirectionResponse::class.java)
            }

        } catch (t: Throwable) {
            Timber.d(t)
        }
        return p2Shop
    }
}