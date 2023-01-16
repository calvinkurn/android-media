package com.tokopedia.review.feature.bulk_write_review.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.flow.FlowUseCase
import com.tokopedia.review.feature.bulk_write_review.domain.model.BulkReviewSubmitRequestParam
import com.tokopedia.review.feature.bulk_write_review.domain.model.BulkReviewSubmitRequestState
import com.tokopedia.review.feature.bulk_write_review.domain.model.BulkReviewSubmitResponse
import com.tokopedia.usecase.RequestParams
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class BulkReviewSubmitUseCase @Inject constructor(
    dispatchers: CoroutineDispatchers,
    private val repository: GraphqlRepository
) : FlowUseCase<List<BulkReviewSubmitRequestParam>, BulkReviewSubmitRequestState>(dispatchers.io) {

    companion object {
        private const val PARAM_REVIEW_SUBMISSIONS = "reviewSubmission"
    }

    override fun graphqlQuery(): String {
        return """
            mutation SubmitBulkReview(${'$'}$PARAM_REVIEW_SUBMISSIONS: [productrevReviewSubmission]) {
              productrevBulkSubmitProductReview($PARAM_REVIEW_SUBMISSIONS: ${'$'}$PARAM_REVIEW_SUBMISSIONS) {
                success
                failedInboxIDs
              }
            }
        """.trimIndent()
    }

    override suspend fun execute(
        params: List<BulkReviewSubmitRequestParam>
    ) = flow {
        emit(BulkReviewSubmitRequestState.Requesting())
        emit(BulkReviewSubmitRequestState.Complete.Success(params, sendRequest(params).productRevBulkSubmitProductReview))
    }.catch {
        emit(BulkReviewSubmitRequestState.Complete.Error(it))
    }

    private suspend fun sendRequest(
        params: List<BulkReviewSubmitRequestParam>
    ): BulkReviewSubmitResponse.Data {
        return repository.request(graphqlQuery(), createRequestParam(params))
    }

    private fun createRequestParam(params: List<BulkReviewSubmitRequestParam>): Map<String, Any> {
        return RequestParams.create().apply {
            putObject(PARAM_REVIEW_SUBMISSIONS, params)
        }.parameters
    }
}
