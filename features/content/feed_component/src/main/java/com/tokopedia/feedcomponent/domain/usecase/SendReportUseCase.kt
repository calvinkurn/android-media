package com.tokopedia.feedcomponent.domain.usecase

import com.tokopedia.feedcomponent.data.raw.GQL_MUTATION_SEND_REPORT
import com.tokopedia.feedcomponent.domain.model.report.entity.SendReportResponse
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import javax.inject.Inject

class SendReportUseCase @Inject constructor(
        graphqlRepository: GraphqlRepository) : GraphqlUseCase<SendReportResponse>(graphqlRepository) {

    init {
        setTypeClass(SendReportResponse::class.java)
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE).build())
        setGraphqlQuery(GQL_MUTATION_SEND_REPORT)
    }

    fun createRequestParams(contentId: Int, reasonType: String, reasonMessage: String, contentType: String) {
        val params = mutableMapOf(
                PARAM_CONTENT_ID to contentId,
                PARAM_CONTENT_TYPE to contentType,
                PARAM_REASON_TYPE to reasonType,
                PARAM_REASON_MESSAGE to reasonMessage
        )
        setRequestParams(params)
    }

    companion object {
        private const val PARAM_CONTENT_ID = "content"
        private const val PARAM_CONTENT_TYPE = "contentType"
        private const val PARAM_REASON_TYPE = "reasonType"
        private const val PARAM_REASON_MESSAGE = "reasonMessage"
        const val REPORT_SUCCESS = 1
        const val ERROR_REPORT_DUPLICATE = "error_duplicate_report"
    }
}