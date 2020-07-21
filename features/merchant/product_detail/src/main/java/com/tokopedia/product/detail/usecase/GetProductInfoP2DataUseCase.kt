package com.tokopedia.product.detail.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.product.detail.common.ProductDetailCommonConstant
import com.tokopedia.product.detail.data.model.ProductInfoP2Data
import com.tokopedia.product.detail.di.RawQueryKeyConstant
import com.tokopedia.product.detail.view.util.CacheStrategyUtil
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Yehezkiel on 20/07/20
 */
class GetProductInfoP2DataUseCase @Inject constructor(private val rawQueries: Map<String, String>,
                                                      private val graphqlRepository: GraphqlRepository) : UseCase<ProductInfoP2Data>() {
    companion object {
        fun createParams(productId: String, pdpSession: String): RequestParams =
                RequestParams.create().apply {
                    putString(ProductDetailCommonConstant.PARAM_PRODUCT_ID, productId)
                    putString(ProductDetailCommonConstant.PARAM_PDP_SESSION, pdpSession)
                }
    }

    private var requestParams: RequestParams = RequestParams.EMPTY
    private var forceRefresh: Boolean = false

    suspend fun executeOnBackground(requestParams: RequestParams, forceRefresh: Boolean) {
        this.requestParams = requestParams
        this.forceRefresh = forceRefresh
        executeOnBackground()
    }

    override suspend fun executeOnBackground(): ProductInfoP2Data {
        var p2Data = ProductInfoP2Data()
        val p2DataRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_P2_DATA],
                ProductInfoP2Data::class.java, requestParams.parameters)

        try {
            val gqlResponse = graphqlRepository.getReseponse(listOf(p2DataRequest), CacheStrategyUtil.getCacheStrategy(forceRefresh))
            val successData = gqlResponse.getData<ProductInfoP2Data>(ProductInfoP2Data::class.java)
            val errorData = gqlResponse.getError(ProductInfoP2Data::class.java)

            if (successData == null || successData.error.isError || errorData.isNotEmpty()) {
                throw RuntimeException()
            }

            p2Data = successData
        } catch (t: Throwable) {
            Timber.d(t)
        }
        return p2Data
    }
}