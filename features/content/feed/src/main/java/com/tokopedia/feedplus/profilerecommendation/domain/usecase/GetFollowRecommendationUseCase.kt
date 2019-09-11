package com.tokopedia.feedplus.profilerecommendation.domain.usecase

import com.tokopedia.feedplus.profilerecommendation.domain.model.FollowRecommendationResponse
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by jegul on 2019-09-11.
 */
class GetFollowRecommendationUseCase @Inject constructor(
        graphqlRepository: GraphqlRepository,
        @Named(FOLLOW_RECOMMENDATION_QUERY) val query: String
) : GraphqlUseCase<FollowRecommendationResponse>(graphqlRepository) {

    companion object {
        const val FOLLOW_RECOMMENDATION_QUERY = "follow_recommendation_query"

        private const val PARAM_IDS = "ids"
        private const val PARAM_CURSOR = "cursor"

        fun getRequestParams(interestIds: List<Int>, cursor: String): Map<String, Any?> = mapOf(
                PARAM_IDS to interestIds,
                PARAM_CURSOR to cursor
        )
    }

    init {
        setTypeClass(FollowRecommendationResponse::class.java)
        setGraphqlQuery(query)
    }
}