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
        @Named(FOLLOW_ALL_RECOMMENDATION_MUTATION) val query: String
) : GraphqlUseCase<FollowAllRecommendationResponse>(graphqlRepository) {

    companion object {
        const val FOLLOW_ALL_RECOMMENDATION_MUTATION = "follow_all_recommendation_mutation"

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