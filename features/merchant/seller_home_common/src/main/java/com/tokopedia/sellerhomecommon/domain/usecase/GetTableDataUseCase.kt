package com.tokopedia.sellerhomecommon.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql_query_annotation.GqlQuery
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

@GqlQuery("GetTableDataGqlQuery", GetTableDataUseCase.QUERY)
class GetTableDataUseCase(
    graphqlRepository: GraphqlRepository,
    private val tableMapper: TableMapper,
    dispatchers: CoroutineDispatchers
) : CloudAndCacheGraphqlUseCase<GetTableDataResponse, List<TableDataUiModel>>(
    graphqlRepository,
    tableMapper,
    dispatchers,
    GetTableDataGqlQuery()
) {

    override val classType: Class<GetTableDataResponse>
        get() = GetTableDataResponse::class.java

    override suspend fun executeOnBackground(requestParams: RequestParams, includeCache: Boolean) {
        super.executeOnBackground(requestParams, includeCache).also { isFirstLoad = false }
    }

    override suspend fun executeOnBackground(): List<TableDataUiModel> {
        val dataKeys = (params.getObject(DATA_KEYS) as? List<DataKeyModel>).orEmpty()
        val gqlRequest = GraphqlRequest(graphqlQuery, classType, params.parameters)
        val gqlResponse = graphqlRepository.response(listOf(gqlRequest), cacheStrategy)

        val errors = gqlResponse.getError(classType)
        if (errors.isNullOrEmpty()) {
            val data = gqlResponse.getData<GetTableDataResponse>()
            val isFromCache = cacheStrategy.type == CacheType.CACHE_ONLY
            tableMapper.setDataKeys(dataKeys)
            return tableMapper.mapRemoteDataToUiData(data, isFromCache)
        } else {
            throw MessageErrorException(errors.firstOrNull()?.message.orEmpty())
        }
    }

    companion object {
        internal const val QUERY = """
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
                        meta
                        iconUrl
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
        """
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
    }
}
