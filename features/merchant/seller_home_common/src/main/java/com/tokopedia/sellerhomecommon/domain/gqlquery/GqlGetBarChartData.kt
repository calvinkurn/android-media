package com.tokopedia.sellerhomecommon.domain.gqlquery

import com.tokopedia.gql_query_annotation.GqlQueryInterface

/**
 * Created by @ilhamsuaib on 17/01/22.
 */

object GqlGetBarChartData : GqlQueryInterface {

    private const val TOP_OPERATION_NAME = "getBarChartData"
    private const val OPERATION_NAME = "fetchBarChartWidgetData"
    val QUERY = """
        query $TOP_OPERATION_NAME(${'$'}dataKeys: [dataKey!]!) {
          $OPERATION_NAME(dataKeys: ${'$'}dataKeys) {
            data {
              dataKey
              data {
                summary {
                  value
                  valueFmt
                  diffPercentage
                  diffPercentageFmt
                }
                metrics {
                  name
                  value {
                    value
                    valueFmt
                    color
                  }
                }
                axes {
                  yLabel {
                    value
                    valueFmt
                  }
                  xLabel {
                    value
                    valueFmt
                  }
                }
              }
              errorMsg
              showWidget
            }
          }
        }
    """.trimIndent()

    override fun getOperationNameList(): List<String> {
        return listOf(OPERATION_NAME)
    }

    override fun getQuery(): String = QUERY

    override fun getTopOperationName(): String = TOP_OPERATION_NAME
}
