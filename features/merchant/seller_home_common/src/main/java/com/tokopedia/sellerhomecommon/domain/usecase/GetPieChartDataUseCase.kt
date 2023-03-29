package com.tokopedia.sellerhomecommon.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sellerhomecommon.domain.mapper.PieChartMapper
import com.tokopedia.sellerhomecommon.domain.model.DataKeyModel
import com.tokopedia.sellerhomecommon.domain.model.ParamCommonWidgetModel
import com.tokopedia.sellerhomecommon.domain.model.GetPieChartDataResponse
import com.tokopedia.sellerhomecommon.presentation.model.PieChartDataUiModel
import com.tokopedia.usecase.RequestParams

/**
 * Created By @ilhamsuaib on 06/07/20
 */

@GqlQuery("GetPieChartDataGqlQuery", GetPieChartDataUseCase.QUERY)
class GetPieChartDataUseCase(
    gqlRepository: GraphqlRepository,
    mapper: PieChartMapper,
    dispatchers: CoroutineDispatchers
) : CloudAndCacheGraphqlUseCase<GetPieChartDataResponse, List<PieChartDataUiModel>>(
    gqlRepository, mapper, dispatchers, GetPieChartDataGqlQuery()
) {

    override val classType: Class<GetPieChartDataResponse>
        get() = GetPieChartDataResponse::class.java

    override suspend fun executeOnBackground(requestParams: RequestParams, includeCache: Boolean) {
        super.executeOnBackground(requestParams, includeCache).also { isFirstLoad = false }
    }

    override suspend fun executeOnBackground(): List<PieChartDataUiModel> {
        val gqlRequest = GraphqlRequest(graphqlQuery, classType, params.parameters)
        val gqlResponse = graphqlRepository.response(listOf(gqlRequest), cacheStrategy)

        val errors = gqlResponse.getError(classType)
        if (errors.isNullOrEmpty()) {
            val data = gqlResponse.getData<GetPieChartDataResponse>()
            return mapper.mapRemoteDataToUiData(data, cacheStrategy.type == CacheType.CACHE_ONLY)
        } else {
            throw MessageErrorException(errors.firstOrNull()?.message.orEmpty())
        }
    }

    companion object {
        internal const val QUERY = """
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