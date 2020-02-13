package com.tokopedia.brandlist.brandlist_search.domain

import com.tokopedia.brandlist.brandlist_search.data.model.BrandListSearchRecommendationResponse
import com.tokopedia.brandlist.common.GQLQueryConstant
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject
import javax.inject.Named

class SearchRecommedationBrandUseCase @Inject constructor(
        private val graphqlUseCase: MultiRequestGraphqlUseCase,
        @Named(GQLQueryConstant.QUERY_BRANDLIST_POPULAR_BRAND) val query: String
): UseCase<BrandListSearchRecommendationResponse>() {

    var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): BrandListSearchRecommendationResponse {
        val gqlRequest = GraphqlRequest(query, BrandListSearchRecommendationResponse::class.java, params.parameters)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(gqlRequest)
        val graphqlResponse = graphqlUseCase.executeOnBackground()
        val error = graphqlResponse.getError(BrandListSearchRecommendationResponse::class.java) ?: listOf()
        if (error == null || error.isEmpty()) {
            return graphqlResponse.run {
                getData<BrandListSearchRecommendationResponse>(BrandListSearchRecommendationResponse::class.java)
            }
        } else {
            throw MessageErrorException(error.mapNotNull {
                it.message
            }.joinToString(separator = ", "))
        }
    }

    companion object {
        private const val USER_ID = "userID"
        private const val DEVICE = "device"
        private const val WIDGET_NAME = "widgetName"
        private const val WIDGET_NAME_VALUE = "RECOM"
        private const val CATEGORY_ID = "categoryIDs"
        private const val DEVICE_VALUE_ANDROID = 4 // Android

        fun createRequestParam(
                userId: Int?,
                categoryIds: String
        ) = RequestParams.create().apply {
            putInt(USER_ID, userId ?: 0)
            putInt(DEVICE, DEVICE_VALUE_ANDROID)
            putString(WIDGET_NAME, WIDGET_NAME_VALUE)
            putString(CATEGORY_ID, categoryIds)
        }
    }
}