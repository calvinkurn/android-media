package com.tokopedia.sellerhomecommon.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sellerhomecommon.domain.mapper.MultiLineGraphMapper
import com.tokopedia.sellerhomecommon.domain.model.DataKeyModel
import com.tokopedia.sellerhomecommon.domain.model.ParamCommonWidgetModel
import com.tokopedia.sellerhomecommon.domain.model.GetMultiLineGraphResponse
import com.tokopedia.sellerhomecommon.presentation.model.MultiLineGraphDataUiModel
import com.tokopedia.usecase.RequestParams

/**
 * Created By @ilhamsuaib on 26/10/20
 */

@GqlQuery("GetMultiLineGraphGqlQuery", GetMultiLineGraphUseCase.QUERY)
class GetMultiLineGraphUseCase(
    gqlRepository: GraphqlRepository,
    mapper: MultiLineGraphMapper,
    dispatchers: CoroutineDispatchers
) : CloudAndCacheGraphqlUseCase<GetMultiLineGraphResponse, List<MultiLineGraphDataUiModel>>(
    gqlRepository, mapper, dispatchers, GetMultiLineGraphGqlQuery()
) {

    override val classType: Class<GetMultiLineGraphResponse>
        get() = GetMultiLineGraphResponse::class.java

    override suspend fun executeOnBackground(requestParams: RequestParams, includeCache: Boolean) {
        super.executeOnBackground(requestParams, includeCache).also { isFirstLoad = false }
    }

    override suspend fun executeOnBackground(): List<MultiLineGraphDataUiModel> {
        val gqlRequest = GraphqlRequest(
            graphqlQuery, classType, params.parameters
        )
        val gqlResponse = graphqlRepository.response(listOf(gqlRequest), cacheStrategy)

        val errors = gqlResponse.getError(classType)
        if (errors.isNullOrEmpty()) {
            val data = gqlResponse.getData<GetMultiLineGraphResponse>()
            val isFromCache = cacheStrategy.type == CacheType.CACHE_ONLY
            return mapper.mapRemoteDataToUiData(data, isFromCache)
        } else {
            throw MessageErrorException(errors.firstOrNull()?.message.orEmpty())
        }
    }

    companion object {
        internal const val QUERY = """
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
        """
        private const val DATA_KEYS = "dataKeys"

        fun getRequestParams(
            dataKey: List<String>, dynamicParam: ParamCommonWidgetModel
        ): RequestParams = RequestParams.create().apply {
            val dataKeys = dataKey.map {
                DataKeyModel(
                    key = it, jsonParams = dynamicParam.toJsonString()
                )
            }
            putObject(DATA_KEYS, dataKeys)
        }
    }
}