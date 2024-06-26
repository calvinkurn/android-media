package com.tokopedia.review.feature.bulk_write_review.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.flow.FlowUseCase
import com.tokopedia.review.feature.bulk_write_review.domain.model.BulkReviewGetFormRequestParams
import com.tokopedia.review.feature.bulk_write_review.domain.model.BulkReviewGetFormRequestState
import com.tokopedia.review.feature.bulk_write_review.domain.model.BulkReviewGetFormResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class BulkReviewGetFormUseCase @Inject constructor(
    dispatchers: CoroutineDispatchers,
    private val repository: GraphqlRepository
) : FlowUseCase<BulkReviewGetFormRequestParams, BulkReviewGetFormRequestState>(dispatchers.io) {

    companion object {
        private const val ERROR_MESSAGE_REVIEW_ITEMS_IS_EMPTY = "productrevGetBulkForm.list is empty"
    }

    override fun graphqlQuery(): String {
        return """
            query GetBulkReviewForm(${'$'}invoiceNumber: String, ${'$'}utmSource: String) {
                productrevGetBulkForm(invoiceNumber: ${'$'}invoiceNumber, utmSource: ${'$'}utmSource) {
                    themeCopy
                    list {
                        inboxID
                        reputationID
                        orderID
                        shopID
                        product {
                          productID
                          productName
                          productImageURL
                          productVariant {
                            variantID
                            variantName
                          }
                        }
                        timestamp {
                          createTimeFormatted
                        }
                    }
                }
            }
        """.trimIndent()
    }

    override suspend fun execute(
        params: BulkReviewGetFormRequestParams
    ): Flow<BulkReviewGetFormRequestState> = flow {
        emit(BulkReviewGetFormRequestState.Requesting())
        sendRequest(params).productRevGetBulkForm.let { productrevGetBulkForm ->
            if (productrevGetBulkForm.reviewForm.isEmpty()) {
                emit(
                    BulkReviewGetFormRequestState.Complete.Error(
                        IllegalStateException(ERROR_MESSAGE_REVIEW_ITEMS_IS_EMPTY)
                    )
                )
            } else {
                emit(BulkReviewGetFormRequestState.Complete.Success(productrevGetBulkForm))
            }
        }
    }.catch {
        emit(BulkReviewGetFormRequestState.Complete.Error(it))
    }

    private suspend fun sendRequest(
        params: BulkReviewGetFormRequestParams
    ): BulkReviewGetFormResponse.Data {
        return repository.request(graphqlQuery(), params)
    }
}
