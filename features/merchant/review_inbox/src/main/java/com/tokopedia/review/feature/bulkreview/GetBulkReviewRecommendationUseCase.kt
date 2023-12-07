package com.tokopedia.review.feature.bulkreview

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject

class GetBulkReviewRecommendationUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<BulkReviewRecommendationWidgetResponse>(graphqlRepository) {
    init {
        setGraphqlQuery(GetBulkReviewRecommendationWidgetQuery)
        setTypeClass(BulkReviewRecommendationWidgetResponse::class.java)
    }

    suspend fun execute(
        userId: String
    ): BulkReviewRecommendationWidget {
        setRequestParams(
            GetBulkReviewRecommendationWidgetQuery.createParams(userId)
        )
        return executeOnBackground().bulkReviewRecommendationWidget
    }
}
