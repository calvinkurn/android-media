package com.tokopedia.review.feature.inbox.pending.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.review.feature.inbox.pending.data.ProductrevWaitForFeedbackSeenResponse
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class ProductrevWaitForFeedbackSeenUseCase @Inject constructor(graphqlRepository: GraphqlRepository) : GraphqlUseCase<ProductrevWaitForFeedbackSeenResponse>(graphqlRepository) {

    companion object {
        const val PARAM_INBOX_REVIEW_ID = "inbox_review_id"
        private val query by lazy {
            """
                query productrevWaitForFeedbackSeen(${'$'}inbox_review_id: Int) {
                  productrevWaitForFeedbackSeen(inbox_review_id: ${'$'}inbox_review_id)
                }
            """.trimIndent()
        }
    }

    init {
        setGraphqlQuery(query)
        setTypeClass(ProductrevWaitForFeedbackSeenResponse::class.java)
    }

    fun setParams(inboxReviewId: Int) {
        setRequestParams(
                RequestParams.create().apply {
                    putInt(PARAM_INBOX_REVIEW_ID, inboxReviewId)
                }.parameters
        )
    }
}