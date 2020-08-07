package com.tokopedia.review.feature.inbox.pending.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.review.feature.inbox.pending.data.ProductrevInboxReviewMarkAsSeenResponseWrapper
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class ProductrevMarkAsSeenUseCase @Inject constructor(graphqlRepository: GraphqlRepository) : GraphqlUseCase<ProductrevInboxReviewMarkAsSeenResponseWrapper>(graphqlRepository) {

    companion object {
        const val PARAM_INBOX_REVIEW_ID = "inboxReviewID"
        private val query by lazy {
            """
                mutation productrevInboxReviewMarkAsSeen(${'$'}inboxReviewID: String!) {
                  productrevInboxReviewMarkAsSeen(inboxReviewID: ${'$'}inboxReviewID) {
                      success
                  }
                }
            """.trimIndent()
        }
    }

    init {
        setGraphqlQuery(query)
        setTypeClass(ProductrevInboxReviewMarkAsSeenResponseWrapper::class.java)
    }

    fun setParams(inboxReviewId: Int) {
        setRequestParams(
                RequestParams.create().apply {
                    putString(PARAM_INBOX_REVIEW_ID, inboxReviewId.toString())
                }.parameters
        )
    }
}