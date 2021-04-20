package com.tokopedia.review.feature.inbox.pending.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.review.feature.inbox.pending.data.ProductrevInboxReviewMarkAsSeenResponseWrapper
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class ProductrevMarkAsSeenUseCase @Inject constructor(graphqlRepository: GraphqlRepository) : GraphqlUseCase<ProductrevInboxReviewMarkAsSeenResponseWrapper>(graphqlRepository) {

    companion object {
        const val PARAM_INBOX_REVIEW_ID = "inboxReviewID"
        const val MARK_AS_SEEN_MUTATION_CLASS_NAME = "MarkAsSeen"
        const val MARK_AS_SEEN_MUTATION =
            """
                mutation productrevInboxReviewMarkAsSeen(${'$'}inboxReviewID: String!) {
                  productrevInboxReviewMarkAsSeen(inboxReviewID: ${'$'}inboxReviewID) {
                      success
                  }
                }
            """
    }

    @GqlQuery(MARK_AS_SEEN_MUTATION_CLASS_NAME, MARK_AS_SEEN_MUTATION)
    fun setParams(inboxReviewId: Long) {
        setGraphqlQuery(MarkAsSeen.GQL_QUERY)
        setTypeClass(ProductrevInboxReviewMarkAsSeenResponseWrapper::class.java)
        setRequestParams(
                RequestParams.create().apply {
                    putString(PARAM_INBOX_REVIEW_ID, inboxReviewId.toString())
                }.parameters
        )
    }
}