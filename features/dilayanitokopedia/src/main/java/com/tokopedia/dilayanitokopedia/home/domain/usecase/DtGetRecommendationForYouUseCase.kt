package com.tokopedia.dilayanitokopedia.home.domain.usecase

import com.tokopedia.dilayanitokopedia.home.domain.model.GetDtHomeRecommendationResponse
import com.tokopedia.dilayanitokopedia.home.domain.model.Product
import com.tokopedia.dilayanitokopedia.home.domain.query.GetDtHomeRecomendationQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject

/**
 * Get Recomendation product for list for mega tab
 * https://tokopedia.atlassian.net/wiki/spaces/HP/pages/2053933208/HPB+Home+-+API+GQL+GraphQL+getHomeRecommendationProductV2
 */

class DtGetRecommendationForYouUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<GetDtHomeRecommendationResponse>(graphqlRepository) {

    companion object {
//        private const val PARAM_PAGE = "page"
//        private const val PARAM_PARAM = "param"
//        private const val PARAM_GROUPIDS = "groupIDs"
//        private const val PARAM_CHANNELIDS = "channelIDs"
//        private const val PARAM_LOCATION = "location"
//
//        private const val PARAM_VALUE_PAGE_DT = "dt"
    }

    init {
        setGraphqlQuery(GetDtHomeRecomendationQuery)
        setTypeClass(GetDtHomeRecommendationResponse::class.java)
    }

    suspend fun execute(
//        token: String = "",
//        numOfChannel: Int = 0,
//        localCacheModel: LocalCacheModel?
    ): List<Product> {
//        setRequestParams(RequestParams.create()
//            .appLinky {
// //                putString(PARAM_PAGE, PARAM_VALUE_PAGE_DT)
// //            putStringng(PARAM_LOCATION, mapLocation(localCacheModel))
//            }.parameters
//        )

        val response = executeOnBackground().response
        return response.products
    }
}
