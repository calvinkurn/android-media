package com.tokopedia.sellerhomecommon.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.sellerhomecommon.domain.mapper.TableMapper
import com.tokopedia.sellerhomecommon.domain.model.DataKeyModel
import com.tokopedia.sellerhomecommon.domain.model.GetTableDataResponse
import com.tokopedia.sellerhomecommon.domain.model.DynamicParameterModel
import com.tokopedia.sellerhomecommon.presentation.model.TableDataUiModel
import com.tokopedia.usecase.RequestParams

/**
 * Created By @ilhamsuaib on 30/06/20
 */

class GetTableDataUseCase(
        private val graphqlRepository: GraphqlRepository,
        private val mapper: TableMapper
) : BaseGqlUseCase<List<TableDataUiModel>>() {

    override suspend fun executeOnBackground(): List<TableDataUiModel> {
        val gqlRequest = GraphqlRequest(QUERY, GetTableDataResponse::class.java, params.parameters)
        val gqlResponse = graphqlRepository.getReseponse(listOf(gqlRequest))

        val errors = gqlResponse.getError(GetTableDataResponse::class.java)
        if (errors.isNullOrEmpty()) {
            val data = gqlResponse.getData<GetTableDataResponse>()
            val tableData = data.fetchSearchTableWidgetData.data
            return mapper.mapRemoteModelToUiModel(tableData)
        } else {
            throw RuntimeException(errors.joinToString(", ") { it.message })
        }
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
            query (${'$'}dataKeys: [dataKey!]!) {
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