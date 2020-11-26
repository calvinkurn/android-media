package com.tokopedia.sellerhomecommon.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.sellerhomecommon.domain.mapper.PieChartMapper
import com.tokopedia.sellerhomecommon.domain.model.DataKeyModel
import com.tokopedia.sellerhomecommon.domain.model.GetPieChartDataResponse
import com.tokopedia.sellerhomecommon.domain.model.PieChartWidgetDataModel
import com.tokopedia.sellerhomecommon.domain.model.DynamicParameterModel
import com.tokopedia.sellerhomecommon.presentation.model.PieChartDataUiModel
import com.tokopedia.usecase.RequestParams

/**
 * Created By @ilhamsuaib on 06/07/20
 */

class GetPieChartDataUseCase(
        private val gqlRepository: GraphqlRepository,
        private val mapper: PieChartMapper
) : BaseGqlUseCase<List<PieChartDataUiModel>>() {

    override suspend fun executeOnBackground(): List<PieChartDataUiModel> {
        val gqlRequest = GraphqlRequest(QUERY, GetPieChartDataResponse::class.java, params.parameters)
        val gqlResponse = gqlRepository.getReseponse(listOf(gqlRequest))

        val errors = gqlResponse.getError(GetPieChartDataResponse::class.java)
        if (errors.isNullOrEmpty()) {
            val data = gqlResponse.getData<GetPieChartDataResponse>()
            val pieChartData: List<PieChartWidgetDataModel> = data.fetchPieChartWidgetData.data
            return mapper.mapRemoteModelToUiModel(pieChartData)
        } else {
            throw RuntimeException(errors.joinToString(", ") { it.message })
        }
    }

    companion object {
        private const val DATA_KEYS = "dataKeys"

        fun getRequestParams(
                dataKey: List<String>,
                dynamicParameter: DynamicParameterModel
        ): RequestParams = RequestParams.create().apply {
            val dataKeys = dataKey.map {
                DataKeyModel(
                        key = it,
                        jsonParams = dynamicParameter.toJsonString()
                )
            }
            putObject(DATA_KEYS, dataKeys)
        }

        private val QUERY = """
            query (${'$'}dataKeys: [dataKey!]!) {
              fetchPieChartWidgetData(dataKeys: ${'$'}dataKeys) {
                data {
                  dataKey
                  data {
                    item {
                      percentage
                      percentageFmt
                      value
                      valueFmt
                      legend
                      color
                    }
                    summary {
                      value
                      valueFmt
                      diffPercentage
                      diffPercentageFmt
                    }
                  }
                  errorMsg
                }
              }
            }
        """.trimIndent()
    }
}