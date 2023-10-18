package com.tokopedia.sellerhomecommon.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sellerhomecommon.domain.mapper.LineGraphMapper
import com.tokopedia.sellerhomecommon.domain.model.DataKeyModel
import com.tokopedia.sellerhomecommon.domain.model.ParamCommonWidgetModel
import com.tokopedia.sellerhomecommon.domain.model.GetLineGraphDataResponse
import com.tokopedia.sellerhomecommon.presentation.model.LineGraphDataUiModel
import com.tokopedia.usecase.RequestParams

/**
 * Created By @ilhamsuaib on 2020-01-27
 */

@GqlQuery("GetLineGraphDataGqlQuery", GetLineGraphDataUseCase.QUERY)
class GetLineGraphDataUseCase(
    gqlRepository: GraphqlRepository,
    lineGraphMapper: LineGraphMapper,
    dispatchers: CoroutineDispatchers
) : CloudAndCacheGraphqlUseCase<GetLineGraphDataResponse, List<LineGraphDataUiModel>>(
    gqlRepository, lineGraphMapper, dispatchers, GetLineGraphDataGqlQuery()
) {

    override val classType: Class<GetLineGraphDataResponse>
        get() = GetLineGraphDataResponse::class.java

    override suspend fun executeOnBackground(requestParams: RequestParams, includeCache: Boolean) {
        super.executeOnBackground(requestParams, includeCache).also { isFirstLoad = false }
    }

    override suspend fun executeOnBackground(): List<LineGraphDataUiModel> {
        val gqlRequest = GraphqlRequest(graphqlQuery, classType, params.parameters)
        val gqlResponse = graphqlRepository.response(listOf(gqlRequest), cacheStrategy)

        val errors: List<GraphqlError>? = gqlResponse.getError(classType)
        if (errors.isNullOrEmpty()) {
            val data = gqlResponse.getData<GetLineGraphDataResponse>()
            val isFromCache = cacheStrategy.type == CacheType.CACHE_ONLY
            return mapper.mapRemoteDataToUiData(data, isFromCache)
        } else {
            throw MessageErrorException(errors.firstOrNull()?.message.orEmpty())
        }
    }

    companion object {
        internal const val QUERY = """
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
                  showWidget
                }
              }
            }
        """
        private const val DATA_KEYS = "dataKeys"

        fun getRequestParams(
            dataKey: List<String>,
            dynamicParam: ParamCommonWidgetModel
        ): RequestParams = RequestParams.create().apply {
            val dataKeys = dataKey.map {
                DataKeyModel(
                    key = it,
                    jsonParams = dynamicParam.toJsonString()
                )
            }
            putObject(DATA_KEYS, dataKeys)
        }
    }
}