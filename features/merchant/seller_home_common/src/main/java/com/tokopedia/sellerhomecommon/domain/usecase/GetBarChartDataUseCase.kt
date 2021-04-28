package com.tokopedia.sellerhomecommon.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.sellerhomecommon.domain.mapper.BarChartMapper
import com.tokopedia.sellerhomecommon.domain.model.DataKeyModel
import com.tokopedia.sellerhomecommon.domain.model.DynamicParameterModel
import com.tokopedia.sellerhomecommon.domain.model.GetBarChartDataResponse
import com.tokopedia.sellerhomecommon.presentation.model.BarChartDataUiModel
import com.tokopedia.usecase.RequestParams

/**
 * Created By @ilhamsuaib on 10/07/20
 */

class GetBarChartDataUseCase(
        gqlRepository: GraphqlRepository,
        mapper: BarChartMapper,
        dispatchers: CoroutineDispatchers
) : CloudAndCacheGraphqlUseCase<GetBarChartDataResponse, List<BarChartDataUiModel>>(
        gqlRepository, mapper, dispatchers, GetBarChartDataResponse::class.java, QUERY, false) {

    override suspend fun executeOnBackground(requestParams: RequestParams, includeCache: Boolean) {
        super.executeOnBackground(requestParams, includeCache).also { isFirstLoad = false }
    }

    override suspend fun executeOnBackground(): List<BarChartDataUiModel> {
        val gqlRequest = GraphqlRequest(QUERY, GetBarChartDataResponse::class.java, params.parameters)
        val gqlResponse = graphqlRepository.getReseponse(listOf(gqlRequest), cacheStrategy)

        val errors = gqlResponse.getError(GetBarChartDataResponse::class.java)
        if (errors.isNullOrEmpty()) {
            val response = gqlResponse.getData<GetBarChartDataResponse>()
            return mapper.mapRemoteDataToUiData(response, cacheStrategy.type == CacheType.CACHE_ONLY)
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
            query getBarChartData(${'$'}dataKeys: [dataKey!]!) {
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
                        color
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
                  showWidget
                }
              }
            }
        """.trimIndent()
    }
}