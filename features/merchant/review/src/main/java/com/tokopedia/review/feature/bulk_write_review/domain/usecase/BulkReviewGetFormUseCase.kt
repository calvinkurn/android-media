package com.tokopedia.review.feature.bulk_write_review.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.flow.FlowUseCase
import com.tokopedia.review.feature.bulk_write_review.domain.model.BulkReviewGetFormRequestState
import com.tokopedia.review.feature.bulk_write_review.domain.model.BulkReviewGetFormResponse
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class BulkReviewGetFormUseCase @Inject constructor(
    dispatchers: CoroutineDispatchers,
    private val repository: GraphqlRepository
) : FlowUseCase<Unit, BulkReviewGetFormRequestState>(dispatchers.io) {

    companion object {
        private const val ERROR_MESSAGE_REVIEW_ITEMS_IS_EMPTY = "productrevGetBulkForm.list is null or empty"
        private const val ERROR_MESSAGE_RESPONSE_IS_NULL = "productrevGetBulkForm is null"
    }

    override fun graphqlQuery(): String {
        return """
            query GetBulkReviewForm {
                productrevGetBulkForm {
                    list {
                        inboxID
                        reputationID
                        orderID
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
        params: Unit
    ) = flow {
        emit(BulkReviewGetFormRequestState.Requesting())
        sendRequest(params).productrevGetBulkForm?.let { productrevGetBulkForm ->
            if (productrevGetBulkForm.reviewForm.isNullOrEmpty()) {
                emit(
                    BulkReviewGetFormRequestState.Complete.Error(
                        IllegalStateException(ERROR_MESSAGE_REVIEW_ITEMS_IS_EMPTY)
                    )
                )
            } else {
                emit(BulkReviewGetFormRequestState.Complete.Success(productrevGetBulkForm))
            }
        } ?: emit(
            BulkReviewGetFormRequestState.Complete.Error(
                IllegalStateException(ERROR_MESSAGE_RESPONSE_IS_NULL)
            )
        )
    }.catch {
        emit(BulkReviewGetFormRequestState.Complete.Error(it))
    }

    private suspend fun sendRequest(params: Unit): BulkReviewGetFormResponse.Data {
        return repository.request(graphqlQuery(), params)
    }
}
