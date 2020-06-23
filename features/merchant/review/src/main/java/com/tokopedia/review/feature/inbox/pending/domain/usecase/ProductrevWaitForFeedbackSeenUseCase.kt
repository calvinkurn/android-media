package com.tokopedia.review.feature.inbox.pending.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.review.feature.inbox.pending.data.ProductrevWaitForFeedbackSeenResponse
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class ProductrevWaitForFeedbackSeenUseCase @Inject constructor(graphqlRepository: GraphqlRepository) : GraphqlUseCase<ProductrevWaitForFeedbackSeenResponse>(graphqlRepository) {

    companion object {
        const val PARAM_PRODUCT_INBOX_ID = "productInboxID"
        private val query by lazy {
            """
                query productrevWaitForFeedbackSeen(${'$'}productInboxID: Int) {
                  productrevWaitForFeedbackSeen(inbox_review_id: ${'$'}productInboxID)
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
                    putInt(PARAM_PRODUCT_INBOX_ID, inboxReviewId)
                }.parameters
        )
    }
}