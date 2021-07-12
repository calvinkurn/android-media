package com.tokopedia.product.detail.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.product.detail.common.ProductDetailCommonConstant
import com.tokopedia.product.detail.data.model.ProductInfoP3
import com.tokopedia.product.detail.data.model.ticker.GeneralTickerDataModel
import com.tokopedia.product.detail.data.util.DynamicProductDetailMapper
import com.tokopedia.product.detail.data.util.ProductDetailConstant.PARAMS_PAGE
import com.tokopedia.product.detail.data.util.ProductDetailConstant.PARAMS_PAGE_PDP
import com.tokopedia.product.detail.view.util.CacheStrategyUtil
import com.tokopedia.product.detail.view.util.doActionIfNotNull
import com.tokopedia.usecase.coroutines.UseCase
import timber.log.Timber
import javax.inject.Inject

class GetProductInfoP3UseCase @Inject constructor(private val graphqlRepository: GraphqlRepository) : UseCase<ProductInfoP3>() {

    companion object {
        val QUERY_TICKER = """
            query get_ticker(${'$'}page: String!) {
              ticker {
                tickers(page: ${'$'}page) {
                  message
                  layout
                }
              }
            }
        """.trimIndent()

        fun createParams(weight: Float, shopDomain: String?, origin: String?): Map<String, Any?> = mapOf(
                ProductDetailCommonConstant.PARAM_RATE_EST_WEIGHT to weight,
                ProductDetailCommonConstant.PARAM_SHOP_DOMAIN to shopDomain,
                ProductDetailCommonConstant.PARAM_PRODUCT_ORIGIN to origin)
    }

    private var forceRefresh: Boolean = false
    private var isUserSessionActive = false

    suspend fun executeOnBackground(forceRefresh: Boolean, isUserSessionActive: Boolean): ProductInfoP3 {
        this.forceRefresh = forceRefresh
        this.isUserSessionActive = isUserSessionActive
        return executeOnBackground()
    }

    override suspend fun executeOnBackground(): ProductInfoP3 {
        val productInfoP3 = ProductInfoP3()

        val p3Request = mutableListOf<GraphqlRequest>()

        //region Ticker
        val tickerParams = generateTickerParam()
        val tickerRequest = GraphqlRequest(QUERY_TICKER, GeneralTickerDataModel.TickerResponse::class.java, tickerParams)
        p3Request.add(tickerRequest)
        //endregion

        val cacheStrategy = if (!isUserSessionActive) GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build() else CacheStrategyUtil.getCacheStrategy(forceRefresh)

        try {
            val response = graphqlRepository.getReseponse(p3Request, cacheStrategy)

            //region Ticker
            if (response.getError(GeneralTickerDataModel.TickerResponse::class.java)?.isNotEmpty() != true) {
                response.doActionIfNotNull<GeneralTickerDataModel.TickerResponse> {
                    productInfoP3.tickerInfo = DynamicProductDetailMapper.getTickerInfoData(it)
                }
            }
            //endregion

        } catch (t: Throwable) {
            Timber.d(t)
        }

        return productInfoP3
    }

    private fun generateTickerParam(): Map<String, Any> {
        return mapOf(PARAMS_PAGE to PARAMS_PAGE_PDP)
    }
}