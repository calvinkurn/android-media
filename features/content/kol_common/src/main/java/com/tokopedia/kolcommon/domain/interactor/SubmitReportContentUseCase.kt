package com.tokopedia.kolcommon.domain.interactor

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.kolcommon.data.SubmitReportContentResponse
import javax.inject.Inject

@GqlQuery(SubmitReportContentUseCase.QUERY_NAME, SubmitReportContentUseCase.QUERY)
class SubmitReportContentUseCase @Inject constructor(
        graphqlRepository: GraphqlRepository
) : GraphqlUseCase<SubmitReportContentResponse>(graphqlRepository) {

    init {
        setTypeClass(SubmitReportContentResponse::class.java)
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE).build())
        setGraphqlQuery(SubmitReportContentUseCaseQuery())
    }

    companion object {

        private const val PARAM_CONTENT_ID = "content"
        private const val PARAM_CONTENT_TYPE = "contentType"
        private const val PARAM_REASON_TYPE = "reasonType"
        private const val PARAM_REASON_MESSAGE = "reasonMessage"

        private const val VALUE_CONTENT_TYPE = "content"

        const val QUERY_NAME = "SubmitReportContentUseCaseQuery"
        const val QUERY = """
            mutation SubmitReportContent(${'$'}$PARAM_CONTENT_ID: Int!, ${'$'}$PARAM_CONTENT_TYPE: String!, ${'$'}$PARAM_REASON_TYPE: String!, ${'$'}$PARAM_REASON_MESSAGE: String!) {
              feed_report_submit(
                  content: ${'$'}$PARAM_CONTENT_ID, 
                  contentType: ${'$'}$PARAM_CONTENT_TYPE, 
                  reasonType: ${'$'}$PARAM_REASON_TYPE, 
                  reasonMessage: ${'$'}$PARAM_REASON_MESSAGE
              ) {
                data {
                  success
                }
                error
                error_message
                error_type
              }
            }
            """

        fun createParam(
            contentId: String,
            reasonType: String,
            reasonMessage: String,
        ): Map<String, Any> {
            val content = contentId.toIntOrNull()?:0
            return mapOf(
                PARAM_CONTENT_ID to content,
                PARAM_CONTENT_TYPE to VALUE_CONTENT_TYPE,
                PARAM_REASON_TYPE to reasonType,
                PARAM_REASON_MESSAGE to reasonMessage
            )
        }
    }
}