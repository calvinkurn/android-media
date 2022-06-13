package com.tokopedia.report.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.report.data.model.SubmitReportParams
import com.tokopedia.report.data.model.SubmitReportResponseWrapper
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class SubmitReportUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<SubmitReportResponseWrapper>(graphqlRepository) {
    companion object {
        private const val PARAM_PRODUCT_ID = "product_id"
        private const val PARAM_CATEGORY_ID = "category_id"
        private const val PARAM_FIELDS = "additional_fields"
        private const val PARAM_INPUT = "input"

        private const val SUBMIT_REPORT_QUERY_CLASS_NAME = "SubmitReport"
        private const val SUBMIT_REPORT_QUERY = """
            mutation submitReport(${'$'}input: ReportProductRequest!){
              visionSaveReportProduct(input: ${'$'}input) {
                  status
                  server_process_time
                }
            }
        """
    }

    init {
        init()
    }

    @GqlQuery(SUBMIT_REPORT_QUERY_CLASS_NAME, SUBMIT_REPORT_QUERY)
    private fun init() {
        setTypeClass(SubmitReportResponseWrapper::class.java)
        setGraphqlQuery(SubmitReport.GQL_QUERY)
    }

    fun setParams(params: SubmitReportParams) {
        val mapParams = mapOf(
            PARAM_PRODUCT_ID to params.productId,
            PARAM_CATEGORY_ID to params.categoryId,
            PARAM_FIELDS to params.fields
        )
        setRequestParams(RequestParams.create().apply {
            putObject(PARAM_INPUT, mapParams)
        }.parameters)
    }
}