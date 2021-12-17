package com.tokopedia.review.feature.credibility.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.review.feature.credibility.data.ReviewerCredibilityStatsResponse
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

@GqlQuery(
    GetReviewerCredibilityUseCase.REVIEW_CREDIBILITY_QUERY_CLASS_NAME,
    GetReviewerCredibilityUseCase.REVIEW_CREDIBILITY_QUERY
)
class GetReviewerCredibilityUseCase @Inject constructor(gqlRepository: GraphqlRepository) :
    GraphqlUseCase<ReviewerCredibilityStatsResponse>(gqlRepository) {

    companion object {
        const val PARAM_USER_ID = "userID"
        const val PARAM_ENTRY_POINT = "entrypoint"
        const val REVIEW_CREDIBILITY_QUERY_CLASS_NAME = "ReviewerCredibilityQuery"
        const val REVIEW_CREDIBILITY_QUERY = """
                query getReviewerCredibilityStats(${'$'}userID: String!, ${'$'}entrypoint: String) {
                  productrevGetReviewerCredibilityStats(userID: ${'$'}userID, entrypoint: ${'$'}entrypoint) {
                    label {
                      userName
                      joinDate
                      subtitle
                      footer
                      ctaText
                      infoText
                      ctaApplink
                    }
                    stats {
                      key
                      title
                      imageURL
                      count
                      countFmt
                      show
                    }
                  }
                }
            """
    }

    init {
        setGraphqlQuery(ReviewerCredibilityQuery.GQL_QUERY)
        setTypeClass(ReviewerCredibilityStatsResponse::class.java)
    }

    fun setParams(userId: String, source: String) {
        setRequestParams(
            RequestParams.create().apply {
                putString(PARAM_USER_ID, userId)
                putString(PARAM_ENTRY_POINT, source)
            }.parameters
        )
    }
}