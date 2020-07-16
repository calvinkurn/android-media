package com.tokopedia.sellerhomecommon.domain.usecase

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.sellerhomecommon.domain.mapper.BarChartMapper
import com.tokopedia.sellerhomecommon.domain.model.BarChartWidgetDataModel
import com.tokopedia.sellerhomecommon.domain.model.DataKeyModel
import com.tokopedia.sellerhomecommon.domain.model.GetBarChartDataResponse
import com.tokopedia.sellerhomecommon.domain.model.WidgetDataParameterModel
import com.tokopedia.sellerhomecommon.presentation.model.BarChartDataUiModel
import com.tokopedia.usecase.RequestParams

/**
 * Created By @ilhamsuaib on 10/07/20
 */

class GetBarChartDataUseCase(
        private val gqlRepository: GraphqlRepository,
        private val mapper: BarChartMapper
) : BaseGqlUseCase<List<BarChartDataUiModel>>() {

    override suspend fun executeOnBackground(): List<BarChartDataUiModel> {
        val gqlRequest = GraphqlRequest(QUERY, GetBarChartDataResponse::class.java, params.parameters)
        val gqlResponse = gqlRepository.getReseponse(listOf(gqlRequest))

        val errors = gqlResponse.getError(GetBarChartDataResponse::class.java)
        if (errors.isNullOrEmpty()) {
            val response = gqlResponse.getData<GetBarChartDataResponse>()
            val barChartDataList: List<BarChartWidgetDataModel> = response.fetchBarChartWidgetData.data
            return mapper.mapRemoteModelToUiModel(barChartDataList)
        } else {
            throw MessageErrorException(errors.joinToString(", ") { it.message })
        }
    }

    companion object {
        private const val DATA_KEYS = "dataKeys"

        fun getRequestParams(
                dataKey: List<String>,
                dynamicParameter: WidgetDataParameterModel
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
              fetchBarChartWidgetData(dataKeys: ${'$'}dataKeys) {
                data {
                  dataKey
                  data {
                    summary {
                      value
                      valueFmt
                      diffPercentage
                      diffPercentageFmt
                    }
                    metrics {
                      name
                      value {
                        value
                        valueFmt
                      }
                    }
                    axes {
                      yLabel {
                        value
                        valueFmt
                      }
                      xLabel {
                        value
                        valueFmt
                      }
                    }
                  }
                  errorMsg
                }
              }
            }
        """.trimIndent()
    }
}