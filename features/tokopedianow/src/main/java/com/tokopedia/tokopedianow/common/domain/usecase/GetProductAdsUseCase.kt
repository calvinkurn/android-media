package com.tokopedia.tokopedianow.common.domain.usecase

import com.tokopedia.discovery.common.utils.UrlParamUtils
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokopedianow.common.domain.model.GetProductAdsResponse
import com.tokopedia.tokopedianow.common.domain.model.GetProductAdsResponse.ProductAdsResponse
import com.tokopedia.tokopedianow.common.domain.query.GetProductAdsQuery
import com.tokopedia.tokopedianow.common.domain.query.GetProductAdsQuery.DISPLAY_PARAMS
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetProductAdsUseCase @Inject constructor(graphqlRepository: GraphqlRepository) {

    private val graphql by lazy { GraphqlUseCase<GetProductAdsResponse>(graphqlRepository) }

    suspend fun execute(queryParams: Map<String?, Any>): ProductAdsResponse {
        graphql.apply {
            val requestParams = RequestParams().apply {
                putString(DISPLAY_PARAMS, UrlParamUtils.generateUrlParamString(queryParams))
            }.parameters

            setGraphqlQuery(GetProductAdsQuery)
            setTypeClass(GetProductAdsResponse::class.java)
            setRequestParams(requestParams)
        }
        return graphql.executeOnBackground().productAds
    }
}
