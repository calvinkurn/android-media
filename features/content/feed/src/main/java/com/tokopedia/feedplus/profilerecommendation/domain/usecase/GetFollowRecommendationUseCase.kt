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
        @Named(QUERY_FOLLOW_RECOMMENDATION) val query: String
) : GraphqlUseCase<FollowRecommendationResponse>(graphqlRepository) {

    companion object {
        const val QUERY_FOLLOW_RECOMMENDATION = "query_follow_recommendation"

        private const val PARAM_IDS = "ids"
        private const val PARAM_CURSOR = "cursor"

        fun getRequestParams(interestIds: IntArray, cursor: String): Map<String, Any?> = mapOf(
                PARAM_IDS to interestIds,
                PARAM_CURSOR to cursor
        )
    }

    init {
        setTypeClass(FollowRecommendationResponse::class.java)
        setGraphqlQuery(query)
    }
}