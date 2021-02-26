package com.tokopedia.sellerhomecommon.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.sellerhomecommon.domain.mapper.PieChartMapper
import com.tokopedia.sellerhomecommon.domain.model.DataKeyModel
import com.tokopedia.sellerhomecommon.domain.model.DynamicParameterModel
import com.tokopedia.sellerhomecommon.domain.model.GetPieChartDataResponse
import com.tokopedia.sellerhomecommon.presentation.model.PieChartDataUiModel
import com.tokopedia.usecase.RequestParams

/**
 * Created By @ilhamsuaib on 06/07/20
 */

class GetPieChartDataUseCase(
        gqlRepository: GraphqlRepository,
        mapper: PieChartMapper
) : CloudAndCacheGraphqlUseCase<GetPieChartDataResponse, List<PieChartDataUiModel>>(gqlRepository, mapper, true, GetPieChartDataResponse::class.java, QUERY, false) {

    var firstLoad: Boolean = true

    override suspend fun executeOnBackground(requestParams: RequestParams, includeCache: Boolean) {
        return super.executeOnBackground(requestParams, includeCache).also { firstLoad = false }
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
            query getPieChartData(${'$'}dataKeys: [dataKey!]!) {
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