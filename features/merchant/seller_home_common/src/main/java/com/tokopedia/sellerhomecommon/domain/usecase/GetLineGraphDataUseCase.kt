package com.tokopedia.sellerhomecommon.domain.usecase

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.sellerhomecommon.domain.mapper.LineGraphMapper
import com.tokopedia.sellerhomecommon.domain.model.DataKeyModel
import com.tokopedia.sellerhomecommon.domain.model.GetLineGraphDataResponse
import com.tokopedia.sellerhomecommon.domain.model.DynamicParameterModel
import com.tokopedia.sellerhomecommon.presentation.model.LineGraphDataUiModel
import com.tokopedia.usecase.RequestParams

/**
 * Created By @ilhamsuaib on 2020-01-27
 */

class GetLineGraphDataUseCase(
        private val gqlRepository: GraphqlRepository,
        private val lineGraphMapper: LineGraphMapper
) : BaseGqlUseCase<List<LineGraphDataUiModel>>() {

    override suspend fun executeOnBackground(): List<LineGraphDataUiModel> {
        val gqlRequest = GraphqlRequest(QUERY, GetLineGraphDataResponse::class.java, params.parameters)
        val gqlResponse: GraphqlResponse = gqlRepository.getReseponse(listOf(gqlRequest))

        val errors: List<GraphqlError>? = gqlResponse.getError(GetLineGraphDataResponse::class.java)
        if (errors.isNullOrEmpty()) {
            val data = gqlResponse.getData<GetLineGraphDataResponse>()
            val widgetDataList = data.getLineGraphData?.widgetData.orEmpty()
            return lineGraphMapper.mapRemoteDataModelToUiDataModel(widgetDataList)
        } else {
            throw MessageErrorException(errors.joinToString(", ") { it.message })
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