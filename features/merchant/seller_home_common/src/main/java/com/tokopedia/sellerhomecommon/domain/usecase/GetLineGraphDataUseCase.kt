package com.tokopedia.sellerhomecommon.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
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
        lineGraphMapper: LineGraphMapper,
        dispatchers: CoroutineDispatchers
) : CloudAndCacheGraphqlUseCase<GetLineGraphDataResponse, List<LineGraphDataUiModel>>(
        gqlRepository, lineGraphMapper, dispatchers, GetLineGraphDataResponse::class.java, QUERY, false) {

    override suspend fun executeOnBackground(requestParams: RequestParams, includeCache: Boolean) {
        super.executeOnBackground(requestParams, includeCache).also { isFirstLoad = false }
    }

    override suspend fun executeOnBackground(): List<LineGraphDataUiModel> {
        val gqlRequest = GraphqlRequest(QUERY, GetLineGraphDataResponse::class.java, params.parameters)
        val gqlResponse: GraphqlResponse = graphqlRepository.getReseponse(listOf(gqlRequest), cacheStrategy)

        val errors: List<GraphqlError>? = gqlResponse.getError(GetLineGraphDataResponse::class.java)
        if (errors.isNullOrEmpty()) {
            val data = gqlResponse.getData<GetLineGraphDataResponse>()
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
            """.trimIndent()
    }
}