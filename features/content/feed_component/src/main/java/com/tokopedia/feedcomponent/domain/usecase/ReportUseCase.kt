package com.tokopedia.feedcomponent.domain.usecase


import com.tokopedia.feedcomponent.data.raw.GQL_MUTATION_SEND_REPORT
import com.tokopedia.feedcomponent.domain.model.report.entity.SendReportResponse
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class ReportUseCase @Inject constructor(
        private var graphqlRepository: GraphqlRepository): GraphqlUseCase<SendReportResponse>(graphqlRepository) {

    init {
        setTypeClass(SendReportResponse::class.java)
        setCacheStrategy(
                GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE)
                        .setSessionIncluded(true)
                        .build())
        setGraphqlQuery(GQL_MUTATION_SEND_REPORT)
    }

    companion object {
        private const val PARAM_CONTENT_ID = "content"
        private const val PARAM_CONTENT_TYPE = "contentType"
        private const val PARAM_REASON_TYPE = "reasonType"
        private const val PARAM_REASON_MESSAGE = "reasonMessage"

        private const val CONTENT_TYPE_CONTENT = "content"

        const val REPORT_SUCCESS = 1
        const val ERROR_REPORT_DUPLICATE = "error_duplicate_report"

        fun createRequestParams(contentId: Int, reasonType: String, reasonMessage: String)
                : RequestParams {
            val params = RequestParams.create()
            params.putInt(PARAM_CONTENT_ID, contentId)
            params.putString(PARAM_CONTENT_TYPE, CONTENT_TYPE_CONTENT)
            params.putString(PARAM_REASON_TYPE, reasonType)
            params.putString(PARAM_REASON_MESSAGE, reasonMessage)
            return params
        }
    }
}