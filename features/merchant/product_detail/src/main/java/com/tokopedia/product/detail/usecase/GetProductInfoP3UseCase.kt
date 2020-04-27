package com.tokopedia.product.detail.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.product.detail.common.ProductDetailCommonConstant
import com.tokopedia.product.detail.data.model.ProductInfoP3
import com.tokopedia.product.detail.data.model.UserCodStatus
import com.tokopedia.product.detail.di.RawQueryKeyConstant
import com.tokopedia.product.detail.estimasiongkir.data.model.v3.RatesEstimationModel
import com.tokopedia.product.detail.view.util.CacheStrategyUtil
import com.tokopedia.usecase.coroutines.UseCase
import timber.log.Timber
import javax.inject.Inject

class GetProductInfoP3UseCase @Inject constructor(private val rawQueries: Map<String, String>,
                                                  private val graphqlRepository: GraphqlRepository) : UseCase<ProductInfoP3>() {

    private var weight: Float = 0f
    private var shopDomain = ""
    private var needRequestCod = false
    private var origin: String? = null
    private var forceRefresh = false

    fun createRequestParams(weight: Float, shopDomain: String, needRequestCod: Boolean, forceRefresh: Boolean, origin: String?) {
        this.weight = weight
        this.shopDomain = shopDomain
        this.needRequestCod = needRequestCod
        this.origin = origin
        this.forceRefresh = forceRefresh
    }

    override suspend fun executeOnBackground(): ProductInfoP3 {
        val productInfoP3 = ProductInfoP3()

        val estimationParams = mapOf(ProductDetailCommonConstant.PARAM_RATE_EST_WEIGHT to weight,
                ProductDetailCommonConstant.PARAM_RATE_EST_SHOP_DOMAIN to shopDomain, ProductDetailCommonConstant.PARAM_PRODUCT_ORIGIN to origin)
        val estimationRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_GET_RATE_ESTIMATION],
                RatesEstimationModel.Response::class.java, estimationParams)

        val requests = mutableListOf(estimationRequest)

        if (needRequestCod) {
            val userCodParams = mapOf(ProductDetailCommonConstant.PARAM_IS_PDP to true)
            val userCodRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_USER_COD_STATUS],
                    UserCodStatus.Response::class.java, userCodParams)
            requests.add(userCodRequest)
        }

        try {
            val response = graphqlRepository.getReseponse(requests, CacheStrategyUtil.getCacheStrategy(forceRefresh))

            if (response.getError(RatesEstimationModel.Response::class.java)?.isNotEmpty() != true) {
                val ratesEstModel = response.getData<RatesEstimationModel.Response>(RatesEstimationModel.Response::class.java)?.data?.data
                ratesEstModel?.texts?.shopCity = ratesEstModel?.shop?.cityName ?: ""
                productInfoP3.rateEstSummarizeText = ratesEstModel?.texts
                productInfoP3.ratesModel = ratesEstModel?.rates
                productInfoP3.addressModel = ratesEstModel?.address
            }

            if (needRequestCod && response.getError(UserCodStatus.Response::class.java)?.isNotEmpty() != true) {
                productInfoP3.userCod = response.getData<UserCodStatus.Response>(UserCodStatus.Response::class.java)
                        .result.userCodStatus.isCod
            }

        } catch (t: Throwable) {
            Timber.d(t)
        }

        return productInfoP3
    }
}