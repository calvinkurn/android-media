package com.tokopedia.sellerhomecommon.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sellerhomecommon.domain.mapper.TableMapper
import com.tokopedia.sellerhomecommon.domain.model.DataKeyModel
import com.tokopedia.sellerhomecommon.domain.model.DynamicParameterModel
import com.tokopedia.sellerhomecommon.domain.model.GetTableDataResponse
import com.tokopedia.sellerhomecommon.presentation.model.TableDataUiModel
import com.tokopedia.usecase.RequestParams

/**
 * Created By @ilhamsuaib on 30/06/20
 */

class GetTableDataUseCase(
        graphqlRepository: GraphqlRepository,
        mapper: TableMapper,
        dispatchers: CoroutineDispatchers
) : CloudAndCacheGraphqlUseCase<GetTableDataResponse, List<TableDataUiModel>>(
        graphqlRepository, mapper, dispatchers, GetTableDataResponse::class.java, QUERY, false) {

    override suspend fun executeOnBackground(requestParams: RequestParams, includeCache: Boolean) {
        super.executeOnBackground(requestParams, includeCache).also { isFirstLoad = false }
    }

    override suspend fun executeOnBackground(): List<TableDataUiModel> {
        val gqlRequest = GraphqlRequest(QUERY, GetTableDataResponse::class.java, params.parameters)
        val gqlResponse = graphqlRepository.getReseponse(listOf(gqlRequest), cacheStrategy)

        val errors = gqlResponse.getError(GetTableDataResponse::class.java)
        if (errors.isNullOrEmpty()) {
            val data = gqlResponse.getData<GetTableDataResponse>()
            return mapper.mapRemoteDataToUiData(data, cacheStrategy.type == CacheType.CACHE_ONLY)
        } else {
            throw MessageErrorException(errors.joinToString(", ") { it.message })
        }
    }

    companion object {
        private const val DATA_KEYS = "dataKeys"
        private const val DEFAULT_POST_LIMIT = 20

        fun getRequestParams(
                dataKey: List<Pair<String, String>>,
                dynamicParameter: DynamicParameterModel,
                limit: Int = DEFAULT_POST_LIMIT
        ): RequestParams {
            val dataKeys = dataKey.map {
                DataKeyModel(
                        key = it.first,
                        jsonParams = dynamicParameter.copy(limit = limit, tableFilter = it.second).toJsonString()
                )
            }
            return RequestParams.create().apply {
                putObject(DATA_KEYS, dataKeys)
            }
        }

        private val QUERY = """
            query getTableData(${'$'}dataKeys: [dataKey!]!) {
              fetchSearchTableWidgetData(dataKeys: ${'$'}dataKeys) {
                data {
                  dataKey
                  data {
                    headers {
                      title
                      width
                    }
                    rows {
                      columns {
                        value
                        type
                      }
                      id
                    }
                  }
                  error
                  errorMsg
                  showWidget
                }
              }
            }
        """.trimIndent()
    }
}