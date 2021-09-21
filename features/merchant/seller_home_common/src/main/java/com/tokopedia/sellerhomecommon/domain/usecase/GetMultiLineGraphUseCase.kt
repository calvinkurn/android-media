package com.tokopedia.sellerhomecommon.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sellerhomecommon.domain.mapper.MultiLineGraphMapper
import com.tokopedia.sellerhomecommon.domain.model.DataKeyModel
import com.tokopedia.sellerhomecommon.domain.model.DynamicParameterModel
import com.tokopedia.sellerhomecommon.domain.model.GetMultiLineGraphResponse
import com.tokopedia.sellerhomecommon.presentation.model.MultiLineGraphDataUiModel
import com.tokopedia.usecase.RequestParams

/**
 * Created By @ilhamsuaib on 26/10/20
 */

class GetMultiLineGraphUseCase (
        gqlRepository: GraphqlRepository,
        mapper: MultiLineGraphMapper,
        dispatchers: CoroutineDispatchers
): CloudAndCacheGraphqlUseCase<GetMultiLineGraphResponse, List<MultiLineGraphDataUiModel>>(
        gqlRepository, mapper, dispatchers, GetMultiLineGraphResponse::class.java, QUERY, false) {

    override suspend fun executeOnBackground(requestParams: RequestParams, includeCache: Boolean) {
        super.executeOnBackground(requestParams, includeCache).also { isFirstLoad = false }
    }

    override suspend fun executeOnBackground(): List<MultiLineGraphDataUiModel> {
        val gqlRequest = GraphqlRequest(QUERY, GetMultiLineGraphResponse::class.java, params.parameters)
        val gqlResponse: GraphqlResponse = graphqlRepository.getReseponse(listOf(gqlRequest))

        val errors: List<GraphqlError>? = gqlResponse.getError(GetMultiLineGraphResponse::class.java)
        if (errors.isNullOrEmpty()) {
            val data = gqlResponse.getData<GetMultiLineGraphResponse>()
            return mapper.mapRemoteDataToUiData(data, cacheStrategy.type == CacheType.CACHE_ONLY)
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
            query fetchMultiTrendlineWidgetData(${'$'}dataKeys : [dataKey!]!) {
              fetchMultiTrendlineWidgetData(dataKeys: ${'$'}dataKeys) {
                data {
                  dataKey
                  error
                  errorMsg
                  showWidget
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