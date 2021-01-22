package com.tokopedia.review.feature.historydetails.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.review.feature.historydetails.data.InboxReviewInsertReputationResponseWrapper
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class InboxReviewInsertReputationUseCase @Inject constructor(graphqlRepository: GraphqlRepository): GraphqlUseCase<InboxReviewInsertReputationResponseWrapper>(graphqlRepository) {

    companion object {
        const val PARAM_REPUTATION_ID = "reputationId"
        const val PARAM_REPUTATION_SCORE = "reputationScore"
        const val PARAM_USERID = "userId"
        const val PARAM_BUYER_SELLER = "buyerSeller"
        const val BUYER_SELLER_VALUE = 1
        const val INSERT_REPUTATION_QUERY_CLASS_NAME = "InsertReputation"
        const val INSERT_REPUTATION_MUTATION =
            """
                mutation inboxReviewInsertReputation(${'$'}reputationId: Int,${'$'}reputationScore: Int,${'$'}userId: Int,${'$'}buyerSeller: Int) {
                  inboxReviewInsertReputation(reputationId:${'$'}reputationId, reputationScore:${'$'}reputationScore, userId:${'$'}userId, buyerSeller:${'$'}buyerSeller) {
                    success
                  }
                }
            """
    }

    @GqlQuery(INSERT_REPUTATION_QUERY_CLASS_NAME, INSERT_REPUTATION_MUTATION)
    fun setParams(reputationId: Long, reputationScore: Int, userId: Int) {
        setGraphqlQuery(InsertReputation.GQL_QUERY)
        setTypeClass(InboxReviewInsertReputationResponseWrapper::class.java)
        setRequestParams(
                RequestParams.create().apply {
                    putLong(PARAM_REPUTATION_ID, reputationId)
                    putInt(PARAM_REPUTATION_SCORE, reputationScore)
                    putInt(PARAM_USERID, userId)
                    putInt(PARAM_BUYER_SELLER, BUYER_SELLER_VALUE)
                }.parameters
        )
    }
}