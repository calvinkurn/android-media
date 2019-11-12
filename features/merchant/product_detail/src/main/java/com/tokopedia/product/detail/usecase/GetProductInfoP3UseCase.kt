package com.tokopedia.product.detail.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.view.debugTrace
import com.tokopedia.product.detail.common.ProductDetailCommonConstant
import com.tokopedia.product.detail.common.data.model.product.ProductInfo
import com.tokopedia.product.detail.data.model.ProductInfoP3
import com.tokopedia.product.detail.data.model.UserCodStatus
import com.tokopedia.product.detail.data.util.weightInKg
import com.tokopedia.product.detail.di.RawQueryKeyConstant
import com.tokopedia.product.detail.estimasiongkir.data.model.v3.RatesEstimationModel
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetProductInfoP3UseCase @Inject constructor(private val rawQueries: Map<String, String>,
                                                  private val graphqlRepository: GraphqlRepository) : UseCase<ProductInfoP3>() {

    var productInfo: ProductInfo = ProductInfo()
    var shopDomain = ""
    var needRequestCod = false
    var origin = ""
    var forceRefresh = false

    fun createRequestParams(productInfo: ProductInfo, shopDomain: String, needRequestCod: Boolean, origin: String, forceRefresh: Boolean) {
        this.productInfo = productInfo
        this.shopDomain = shopDomain
        this.needRequestCod = needRequestCod
        this.origin = origin
        this.forceRefresh = forceRefresh
    }

    override suspend fun executeOnBackground(): ProductInfoP3 {
        val productInfoP3 = ProductInfoP3()

        val estimationParams = mapOf(ProductDetailCommonConstant.PARAM_RATE_EST_WEIGHT to productInfo.basic.weightInKg,
                ProductDetailCommonConstant.PARAM_RATE_EST_SHOP_DOMAIN to shopDomain, "origin" to origin)
        val estimationRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_GET_RATE_ESTIMATION],
                RatesEstimationModel.Response::class.java, estimationParams, false)

        val requests = mutableListOf(estimationRequest)
        val cacheStrategy = GraphqlCacheStrategy.Builder(if (forceRefresh) CacheType.ALWAYS_CLOUD else CacheType.CACHE_FIRST).build()

        if (needRequestCod) {
            val userCodParams = mapOf("isPDP" to true)
            val userCodRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_USER_COD_STATUS],
                    UserCodStatus.Response::class.java, userCodParams)
            requests.add(userCodRequest)
        }

        try {
            val response = graphqlRepository.getReseponse(requests, cacheStrategy)

            if (response.getError(RatesEstimationModel.Response::class.java)?.isNotEmpty() != true) {
                val ratesEstModel = response.getData<RatesEstimationModel.Response>(RatesEstimationModel.Response::class.java)?.data?.data
                ratesEstModel?.texts?.shopCity = ratesEstModel?.shop?.cityName ?: ""
                productInfoP3.rateEstSummarizeText = ratesEstModel?.texts
                productInfoP3.ratesModel = ratesEstModel?.rates
            }


            if (needRequestCod && response.getError(UserCodStatus.Response::class.java)?.isNotEmpty() != true) {
                productInfoP3.userCod = response.getData<UserCodStatus.Response>(UserCodStatus.Response::class.java)
                        .result.userCodStatus.isCod
            }

        } catch (t: Throwable) {
            t.debugTrace()
        }

        return productInfoP3
    }
}