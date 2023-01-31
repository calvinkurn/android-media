package com.tokopedia.dilayanitokopedia.home.domain.usecase

import com.tokopedia.dilayanitokopedia.home.domain.model.GetDtHomeRecommendationResponse
import com.tokopedia.dilayanitokopedia.home.domain.model.GetHomeRecommendationProductV2
import com.tokopedia.dilayanitokopedia.home.domain.query.GetHomeRecommendationQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

/**
 * Get Recomendation product for list for mega tab
 * https://tokopedia.atlassian.net/wiki/spaces/HP/pages/2053933208/HPB+Home+-+API+GQL+GraphQL+getHomeRecommendationProductV2
 */

class GetRecommendationForYouUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<GetDtHomeRecommendationResponse>(graphqlRepository) {

    companion object {
        private const val PARAM_LOCATION = "location"
        private const val PARAM_TYPE = "type"
        private const val PARAM_PRODUCT_PAGE = "productPage"
        private const val PARAM_PAGE = "page"
        private const val VALUE_PAGE_DT = "dt"
        private const val VALUE_TYPE = "banner,position,banner_ads"
    }

    init {
        setGraphqlQuery(GetHomeRecommendationQuery)
        setTypeClass(GetDtHomeRecommendationResponse::class.java)
    }

    /**
     * https://tokopedia.slack.com/archives/C03VBG627FG/p1672885579202169?thread_ts=1669610989.489449&cid=C03VBG627FG
     */
    suspend fun execute(
        locationParamString: String,
        page: Int
    ): GetHomeRecommendationProductV2 {
        setRequestParams(
            RequestParams.create()
                .apply {
                    putString(PARAM_PAGE, VALUE_PAGE_DT)
                    putString(PARAM_TYPE, VALUE_TYPE)
                    putInt(PARAM_PRODUCT_PAGE, page)
                    putString(PARAM_LOCATION, locationParamString)
                }.parameters
        )

        return executeOnBackground().response
    }
}
