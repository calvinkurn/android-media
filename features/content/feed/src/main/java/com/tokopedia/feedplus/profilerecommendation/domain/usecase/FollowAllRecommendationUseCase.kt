package com.tokopedia.feedplus.profilerecommendation.domain.usecase

import com.tokopedia.feedplus.profilerecommendation.domain.model.FollowAllRecommendationResponse
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by jegul on 2019-09-16.
 */
class FollowAllRecommendationUseCase @Inject constructor(
        graphqlRepository: GraphqlRepository,
        @Named(MUTATION_FOLLOW_ALL_RECOMMENDATION) val query: String
) : GraphqlUseCase<FollowAllRecommendationResponse>(graphqlRepository) {

    companion object {
        const val MUTATION_FOLLOW_ALL_RECOMMENDATION = "mutation_follow_all_recommendation"

        private const val PARAM_IDS = "ids"

        fun getRequestParams(interestIds: IntArray): Map<String, Any?> = mapOf(
                PARAM_IDS to interestIds
        )
    }

    init {
        setTypeClass(FollowAllRecommendationResponse::class.java)
        setGraphqlQuery(query)
    }
}