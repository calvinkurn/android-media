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
import com.tokopedia.sellerhomecommon.domain.model.TableAndPostDataKey
import com.tokopedia.sellerhomecommon.presentation.model.TableDataUiModel
import com.tokopedia.usecase.RequestParams

/**
 * Created By @ilhamsuaib on 30/06/20
 */

class GetTableDataUseCase(
    graphqlRepository: GraphqlRepository,
    private val tableMapper: TableMapper,
    dispatchers: CoroutineDispatchers
) : CloudAndCacheGraphqlUseCase<GetTableDataResponse, List<TableDataUiModel>>(
    graphqlRepository, tableMapper, dispatchers, GetTableDataResponse::class.java, QUERY, false
) {

    override suspend fun executeOnBackground(requestParams: RequestParams, includeCache: Boolean) {
        super.executeOnBackground(requestParams, includeCache).also { isFirstLoad = false }
    }

    override suspend fun executeOnBackground(): List<TableDataUiModel> {
        val dataKays: List<DataKeyModel> = (params.getObject(DATA_KEYS) as? List<DataKeyModel>).orEmpty()
        val gqlRequest = GraphqlRequest(QUERY, GetTableDataResponse::class.java, params.parameters)
        val gqlResponse = graphqlRepository.getReseponse(listOf(gqlRequest), cacheStrategy)

        val errors = gqlResponse.getError(GetTableDataResponse::class.java)
        if (errors.isNullOrEmpty()) {
            val data = gqlResponse.getData<GetTableDataResponse>()
            return tableMapper.mapRemoteDataToUiData(data, cacheStrategy.type == CacheType.CACHE_ONLY, dataKays)
        } else {
            throw MessageErrorException(errors.firstOrNull()?.message.orEmpty())
        }
    }

    companion object {
        private const val DATA_KEYS = "dataKeys"

        fun getRequestParams(
            dataKey: List<TableAndPostDataKey>,
            dynamicParameter: DynamicParameterModel
        ): RequestParams {
            val dataKeys: List<DataKeyModel> = dataKey.map {
                DataKeyModel(
                    key = it.dataKey,
                    jsonParams = dynamicParameter.copy(
                        limit = it.maxData, tableFilter = it.filter
                    ).toJsonString(),
                    maxDisplay = it.maxDisplayPerPage
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