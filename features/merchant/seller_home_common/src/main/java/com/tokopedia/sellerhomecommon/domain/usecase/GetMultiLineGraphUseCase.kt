package com.tokopedia.sellerhomecommon.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.sellerhomecommon.domain.mapper.MultiLineGraphMapper
import com.tokopedia.sellerhomecommon.domain.model.DataKeyModel
import com.tokopedia.sellerhomecommon.domain.model.DynamicParameterModel
import com.tokopedia.sellerhomecommon.domain.model.GetMultiLineGraphResponse
import com.tokopedia.sellerhomecommon.domain.model.MultiTrendlineWidgetDataModel
import com.tokopedia.sellerhomecommon.presentation.model.MultiLineGraphDataUiModel
import com.tokopedia.usecase.RequestParams

/**
 * Created By @ilhamsuaib on 26/10/20
 */

class GetMultiLineGraphUseCase (
        private val gqlRepository: GraphqlRepository,
        private val mapper: MultiLineGraphMapper
): BaseGqlUseCase<List<MultiLineGraphDataUiModel>>() {

    override suspend fun executeOnBackground(): List<MultiLineGraphDataUiModel> {
        val gqlRequest = GraphqlRequest(QUERY, GetMultiLineGraphResponse::class.java, params.parameters)
        val gqlResponse: GraphqlResponse = gqlRepository.getReseponse(listOf(gqlRequest))

        val errors: List<GraphqlError>? = gqlResponse.getError(GetMultiLineGraphResponse::class.java)
        if (errors.isNullOrEmpty()) {
            val data = gqlResponse.getData<GetMultiLineGraphResponse>()
            val multiLineData: List<MultiTrendlineWidgetDataModel>? = data.fetchMultiTrendlineWidgetData?.fetchMultiTrendlineData
            return mapper.mapRemoteModelToUiModel(multiLineData, cacheStrategy.type == CacheType.CACHE_ONLY)
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
            query fetchMultiTrendlineWidgetData(${'$'}dataKeys : [dataKey!]!) {
              fetchMultiTrendlineWidgetData(dataKeys: ${'$'}dataKeys) {
                data {
                  dataKey
                  error
                  errorMsg
                  data {
                    metrics {
                      error
                      errMsg
                      summary {
                        title
                        value
                        state
                        description
                        color
                      }
                      type
                      yAxis {
                        YVal
                        YLabel
                      }
                      line {
                        currentPeriode {
                          YVal
                          YLabel
                          XLabel
                        }
                        lastPeriode {
                          YVal
                          YLabel
                          XLabel
                        }
                      }
                    }
                  }
                }
              }
            }
        """.trimIndent()
    }
}