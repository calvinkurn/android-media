package com.tokopedia.review.feature.bulk_write_review.domain.usecase

import android.util.Log
import com.google.gson.Gson
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.flow.FlowUseCase
import com.tokopedia.review.feature.bulk_write_review.domain.model.BulkReviewSubmitRequestParam
import com.tokopedia.review.feature.bulk_write_review.domain.model.BulkReviewSubmitRequestState
import com.tokopedia.review.feature.bulk_write_review.domain.model.BulkReviewSubmitResponse
import com.tokopedia.usecase.RequestParams
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class BulkReviewSubmitUseCase @Inject constructor(
    dispatchers: CoroutineDispatchers,
    private val repository: GraphqlRepository
) : FlowUseCase<List<BulkReviewSubmitRequestParam>, BulkReviewSubmitRequestState>(dispatchers.io) {

    companion object {
        private const val PARAM_REVIEW_SUBMISSIONS = "reviewSubmissions"
    }

    override fun graphqlQuery(): String {
        return """
            mutation GetBulkReviewForm(${'$'}$PARAM_REVIEW_SUBMISSIONS: [ReviewSubmission!]!) {
              productrevSubmitBulkReview(reviewSubmissions: $PARAM_REVIEW_SUBMISSIONS) {
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
        emit(BulkReviewSubmitRequestState.Complete.Success(params, sendRequest(params)))
    }.catch {
        emit(BulkReviewSubmitRequestState.Complete.Error(it))
    }

    private suspend fun sendRequest(
        params: List<BulkReviewSubmitRequestParam>
    ): BulkReviewSubmitResponse.Data.ProductrevSubmitBulkReview {
//        return repository.request(graphqlQuery(), createRequestParam(params))
        Log.d("ReviewLog", Gson().toJson(createRequestParam(params)))
        delay(5000L)
//        return Gson().fromJson("""
//            {"data":{"productrevSubmitBulkReview":{"success":true,"failedInboxIDs":null}}}
//        """.trimIndent(), BulkReviewSubmitResponse::class.java).data!!.productrevSubmitBulkReview!!
        return Gson().fromJson(
            """
            {"data":{"productrevSubmitBulkReview":{"success":false,"failedInboxIDs":[${params.filterIndexed { index, _ -> index % 2 == 1 }.map { it.inboxID }.joinToString(",") { "\"$it\"" }}]}}}
            """.trimIndent(),
            BulkReviewSubmitResponse::class.java
        ).data!!.productrevSubmitBulkReview!!
//        return Gson().fromJson(
//            """
//            {"data":{"productrevSubmitBulkReview":{"success":false,"failedInboxIDs":["1234567891","1234567893"]}}}
//            """.trimIndent(),
//            BulkReviewSubmitResponse::class.java
//        ).data!!.productrevSubmitBulkReview!!
    }

    private fun createRequestParam(params: List<BulkReviewSubmitRequestParam>): Map<String, Any> {
        return RequestParams.create().apply {
            putObject(PARAM_REVIEW_SUBMISSIONS, params)
        }.parameters
    }
}
