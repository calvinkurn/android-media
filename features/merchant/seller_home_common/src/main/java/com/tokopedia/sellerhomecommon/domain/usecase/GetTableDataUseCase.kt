package com.tokopedia.sellerhomecommon.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
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
        mapper: TableMapper
) : CloudAndCacheGraphqlUseCase<GetTableDataResponse, List<TableDataUiModel>>(graphqlRepository, mapper, true, GetTableDataResponse::class.java, QUERY, false) {

    var firstLoad: Boolean = true

    override suspend fun executeOnBackground(requestParams: RequestParams, includeCache: Boolean) {
        return super.executeOnBackground(requestParams, includeCache).also { firstLoad = false }
    }

    companion object {
        private const val DATA_KEYS = "dataKeys"

        fun getRequestParams(
                dataKey: List<String>,
                dynamicParameter: DynamicParameterModel
        ): RequestParams {
            val dataKeys = dataKey.map {
                DataKeyModel(
                        key = it,
                        jsonParams = dynamicParameter.toJsonString()
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
                }
              }
            }
        """.trimIndent()
    }
}