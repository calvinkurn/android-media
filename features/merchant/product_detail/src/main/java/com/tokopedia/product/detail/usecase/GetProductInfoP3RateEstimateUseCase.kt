package com.tokopedia.product.detail.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.product.detail.common.ProductDetailCommonConstant
import com.tokopedia.product.detail.data.model.ProductInfoP3
import com.tokopedia.product.detail.di.RawQueryKeyConstant
import com.tokopedia.product.detail.estimasiongkir.data.model.v3.RatesEstimationModel
import com.tokopedia.product.detail.view.util.CacheStrategyUtil
import com.tokopedia.usecase.coroutines.UseCase
import timber.log.Timber
import javax.inject.Inject

class GetProductInfoP3RateEstimateUseCase @Inject constructor(private val rawQueries: Map<String, String>,
                                                              private val graphqlRepository: GraphqlRepository) : UseCase<ProductInfoP3>() {


    companion object {
        fun createParams(weight: Float, shopDomain: String, forceRefresh: Boolean,
                         origin: String?): Map<String, Any?> = mapOf(
                ProductDetailCommonConstant.PARAM_RATE_EST_WEIGHT to weight,
                ProductDetailCommonConstant.PARAM_SHOP_DOMAIN to shopDomain,
                ProductDetailCommonConstant.PARAM_PRODUCT_ORIGIN to origin,
                ProductDetailCommonConstant.FORCE_REFRESH to forceRefresh)
    }

    var mapOfParam: Map<String, Any?> = mapOf()

    override suspend fun executeOnBackground(): ProductInfoP3 {
        val productInfoP3 = ProductInfoP3()

        val weight: Float = mapOfParam[ProductDetailCommonConstant.PARAM_RATE_EST_WEIGHT] as? Float
                ?: 0F
        val shopDomain = mapOfParam[ProductDetailCommonConstant.PARAM_SHOP_DOMAIN] as? String ?: ""
        val origin: String? = mapOfParam[ProductDetailCommonConstant.PARAM_PRODUCT_ORIGIN] as? String
        val forceRefresh = mapOfParam[ProductDetailCommonConstant.FORCE_REFRESH] as? Boolean
                ?: false

        val p3Request = mutableListOf<GraphqlRequest>()

        val estimationParams = mapOf(
                ProductDetailCommonConstant.PARAM_RATE_EST_WEIGHT to weight,
                ProductDetailCommonConstant.PARAM_RATE_EST_SHOP_DOMAIN to shopDomain,
                ProductDetailCommonConstant.PARAM_PRODUCT_ORIGIN to origin)
        val estimationRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_GET_RATE_ESTIMATION],
                RatesEstimationModel.Response::class.java, estimationParams)

        p3Request.add(estimationRequest)

        try {
            val response = graphqlRepository.getReseponse(p3Request, CacheStrategyUtil.getCacheStrategy(forceRefresh))

            if (response.getError(RatesEstimationModel.Response::class.java)?.isNotEmpty() != true) {
                val ratesEstModel = response.getData<RatesEstimationModel.Response>(RatesEstimationModel.Response::class.java)?.data?.data
                ratesEstModel?.texts?.shopCity = ratesEstModel?.shop?.cityName ?: ""
                productInfoP3.rateEstSummarizeText = ratesEstModel?.texts
                productInfoP3.ratesModel = ratesEstModel?.rates
                productInfoP3.addressModel = ratesEstModel?.address
            }

        } catch (t: Throwable) {
            Timber.d(t)
        }

        return productInfoP3
    }
}