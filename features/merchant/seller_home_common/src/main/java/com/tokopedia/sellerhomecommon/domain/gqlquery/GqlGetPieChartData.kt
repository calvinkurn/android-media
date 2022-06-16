package com.tokopedia.sellerhomecommon.domain.gqlquery

import com.tokopedia.gql_query_annotation.GqlQueryInterface

/**
 * Created by @ilhamsuaib on 17/01/22.
 */

object GqlGetPieChartData : GqlQueryInterface {

    private const val TOP_OPERATION_NAME = "getPieChartData"
    private const val OPERATION_NAME = "fetchPieChartWidgetData"
    val QUERY = """
        query $TOP_OPERATION_NAME(${'$'}dataKeys: [dataKey!]!) {
          $OPERATION_NAME(dataKeys: ${'$'}dataKeys) {
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
    """.trimIndent()

    override fun getOperationNameList(): List<String> {
        return listOf(OPERATION_NAME)
    }

    override fun getQuery(): String = QUERY

    override fun getTopOperationName(): String = TOP_OPERATION_NAME
}
