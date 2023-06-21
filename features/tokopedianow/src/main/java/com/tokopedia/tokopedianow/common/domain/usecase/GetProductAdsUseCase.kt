package com.tokopedia.tokopedianow.common.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokopedianow.common.domain.model.GetProductAdsResponse
import com.tokopedia.tokopedianow.common.domain.model.GetProductAdsResponse.ProductAdsResponse
import com.tokopedia.tokopedianow.common.domain.param.GetProductAdsParam
import com.tokopedia.tokopedianow.common.domain.query.GetProductAdsQuery
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetProductAdsUseCase @Inject constructor(graphqlRepository: GraphqlRepository) {

    companion object {
        private const val QUERY_PARAMS = "params"
    }

    private val graphql by lazy { GraphqlUseCase<GetProductAdsResponse>(graphqlRepository) }

    suspend fun execute(requestParam: GetProductAdsParam): ProductAdsResponse {
        graphql.apply {
            val requestParams = RequestParams().apply {
                putString(QUERY_PARAMS, requestParam.generateQueryParams())
            }.parameters

            setGraphqlQuery(GetProductAdsQuery)
            setTypeClass(GetProductAdsResponse::class.java)
            setRequestParams(requestParams)
        }
        return graphql.executeOnBackground().productAds
    }
}
