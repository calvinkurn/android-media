package com.tokopedia.sellerhomecommon.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.sellerhomecommon.domain.mapper.MultiLineGraphMapper
import com.tokopedia.sellerhomecommon.presentation.model.MultiLineGraphDataUiModel

/**
 * Created By @ilhamsuaib on 26/10/20
 */

class GetMultiLineGraphUseCase (
        private val gqlRepository: GraphqlRepository,
        private val mapper: MultiLineGraphMapper
): BaseGqlUseCase<List<MultiLineGraphDataUiModel>>() {

    override suspend fun executeOnBackground(): List<MultiLineGraphDataUiModel> {
        return emptyList()
    }

    companion object {

        private val QUERY = """
            query fetchMultiTrendlineWidgetData(${'$'}dataKeys : [dataKey!]!) {
              fetchMultiTrendlineWidgetData(dataKeys: ${'$'}dataKeys) {
                data {
                  dataKey
                  error
                  errorMsg
                  data {
                    metrics {
                      error
                      errMsg
                      summary {
                        title
                        tooltip {
                          show
                          title
                          description
                        }
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
        """.trimIndent()
    }
}