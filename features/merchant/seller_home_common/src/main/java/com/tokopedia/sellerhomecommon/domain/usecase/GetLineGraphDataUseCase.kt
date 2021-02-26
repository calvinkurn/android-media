package com.tokopedia.sellerhomecommon.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.sellerhomecommon.domain.mapper.LineGraphMapper
import com.tokopedia.sellerhomecommon.domain.model.DataKeyModel
import com.tokopedia.sellerhomecommon.domain.model.DynamicParameterModel
import com.tokopedia.sellerhomecommon.domain.model.GetLineGraphDataResponse
import com.tokopedia.sellerhomecommon.presentation.model.LineGraphDataUiModel
import com.tokopedia.usecase.RequestParams

/**
 * Created By @ilhamsuaib on 2020-01-27
 */

class GetLineGraphDataUseCase(
        gqlRepository: GraphqlRepository,
        lineGraphMapper: LineGraphMapper
) : CloudAndCacheGraphqlUseCase<GetLineGraphDataResponse, List<LineGraphDataUiModel>>(gqlRepository, lineGraphMapper, true, GetLineGraphDataResponse::class.java, QUERY, false) {

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
            query getLineGraphData(${'$'}dataKeys: [dataKey!]!) {
              fetchLineGraphWidgetData(dataKeys: ${'$'}dataKeys) {
                data {
                  dataKey
                  description
                  header
                  yLabels {
                    yValPrecise
                    yLabel
                  }
                  list {
                    yValPrecise
                    yLabel
                    xLabel
                  }
                  error
                }
              }
            }
            """.trimIndent()
    }
}