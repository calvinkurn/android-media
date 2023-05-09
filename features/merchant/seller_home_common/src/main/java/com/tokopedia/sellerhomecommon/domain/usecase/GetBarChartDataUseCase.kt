package com.tokopedia.sellerhomecommon.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.sellerhomecommon.domain.mapper.BarChartMapper
import com.tokopedia.sellerhomecommon.domain.model.DataKeyModel
import com.tokopedia.sellerhomecommon.domain.model.ParamCommonWidgetModel
import com.tokopedia.sellerhomecommon.domain.model.GetBarChartDataResponse
import com.tokopedia.sellerhomecommon.presentation.model.BarChartDataUiModel
import com.tokopedia.usecase.RequestParams

/**
 * Created By @ilhamsuaib on 10/07/20
 */

@GqlQuery("GetBarChartDataGqlQuery", GetBarChartDataUseCase.QUERY)
class GetBarChartDataUseCase(
    gqlRepository: GraphqlRepository,
    mapper: BarChartMapper,
    dispatchers: CoroutineDispatchers
) : CloudAndCacheGraphqlUseCase<GetBarChartDataResponse, List<BarChartDataUiModel>>(
    gqlRepository, mapper, dispatchers, GetBarChartDataGqlQuery()
) {

    override val classType: Class<GetBarChartDataResponse>
        get() = GetBarChartDataResponse::class.java

    override suspend fun executeOnBackground(requestParams: RequestParams, includeCache: Boolean) {
        super.executeOnBackground(requestParams, includeCache).also { isFirstLoad = false }
    }

    override suspend fun executeOnBackground(): List<BarChartDataUiModel> {
        val gqlRequest = GraphqlRequest(graphqlQuery, classType, params.parameters)
        val gqlResponse = graphqlRepository.response(listOf(gqlRequest), cacheStrategy)

        val errors = gqlResponse.getError(classType)
        if (errors.isNullOrEmpty()) {
            val response = gqlResponse.getData<GetBarChartDataResponse>()
            val isFromCache = cacheStrategy.type == CacheType.CACHE_ONLY
            return mapper.mapRemoteDataToUiData(response, isFromCache)
        } else {
            throw MessageErrorException(errors.firstOrNull()?.message.orEmpty())
        }
    }

    companion object {
        internal const val QUERY = """
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
        """
        private const val DATA_KEYS = "dataKeys"

        fun getRequestParams(
            dataKey: List<String>,
            dynamicParameter: ParamCommonWidgetModel
        ): RequestParams = RequestParams.create().apply {
            val dataKeys = dataKey.map {
                DataKeyModel(
                    key = it,
                    jsonParams = dynamicParameter.toJsonString()
                )
            }
            putObject(DATA_KEYS, dataKeys)
        }
    }
}