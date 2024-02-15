package com.tokopedia.content.product.preview.data.usecase

import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.GqlParam
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import javax.inject.Inject

/**
 * @author by astidhiyaa on 29/11/23
 */
@GqlQuery(SubmitReportUseCase.QUERY_NAME, SubmitReportUseCase.QUERY)
class SubmitReportUseCase @Inject constructor(
    @ApplicationContext private val repo: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<SubmitReportUseCase.Param, SubmitReportUseCase.ReportSubmissionResponse>(dispatchers.io) {

    private val query: GqlQueryInterface = SubmitReportUseCaseQuery()

    override suspend fun execute(params: Param): ReportSubmissionResponse {
        return repo.request(query, params)
    }

    override fun graphqlQuery(): String = query.getQuery()

    data class Param(
        @SerializedName("feedbackID")
        val reviewId: String,

        @SerializedName("reasonCode")
        val reasonCode: Int,

        @SerializedName("reasonText")
        val reasonText: String,
    ) : GqlParam

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
        const val QUERY_NAME = "SubmitReportUseCaseQuery"
        const val QUERY = """
            mutation reportReview(${'$'}feedbackID: String!, ${'$'}reasonCode: Int!, ${'$'}reasonText: String) {
              productrevReportReview(feedbackID: ${'$'}feedbackID, reasonCode: ${'$'}reasonCode, reasonText: ${'$'}reasonText) {
                success
              }
            }
        """
    }
}
