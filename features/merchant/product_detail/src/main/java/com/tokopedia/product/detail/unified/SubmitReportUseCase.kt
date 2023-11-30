package com.tokopedia.product.detail.unified

import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import javax.inject.Inject

/**
 * @author by astidhiyaa on 29/11/23
 */
class SubmitReportUseCase @Inject constructor(
    @ApplicationContext private val repo: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<Map<String, Any>, SubmitReportUseCase.ReportSubmissionResponse>(dispatchers.io) {

    override suspend fun execute(params: Map<String, Any>): ReportSubmissionResponse {
        return repo.request(graphqlQuery(), params)
    }

    override fun graphqlQuery(): String = QUERY

    fun convertToMap(param: Param) = mapOf(
        REVIEW_ID_PARAM to param.reviewId,
        REASON_CODE_PARAM to param.report.reasonCode,
        REASON_TEXT_PARAM to param.report.text,
    )

    data class Param(
        val reviewId: Int,
        val report: ReportUiModel,
    )

    data class ReportSubmissionResponse(
        @SerializedName("productrevReportReview")
        val data: Data = Data(),
    ) {
        data class Data(
            @SerializedName("success")
            val success: Boolean = false,
        )
    }

    companion object {
        private const val REVIEW_ID_PARAM = "feedbackID"
        private const val REASON_CODE_PARAM = "reasonCode"
        private const val REASON_TEXT_PARAM = "reasonText"

        private const val QUERY = """
            mutation reportReview(${'$'}feedbackID: String!, ${'$'}reasonCode: Int!, ${'$'}reasonText: String) {
              productrevReportReview(feedbackID: ${'$'}feedbackID, reasonCode: ${'$'}reasonCode, reasonText: ${'$'}reasonText) {
                success
              }
            }
        """
    }
}
