package com.tokopedia.sellerhomecommon.domain.gqlquery

import com.tokopedia.gql_query_annotation.GqlQueryInterface

/**
 * Created by @ilhamsuaib on 17/01/22.
 */

object GqlGetMultiLineGraphData : GqlQueryInterface {

    private const val OPERATION_NAME = "fetchMultiTrendlineWidgetData"

    val QUERY = """
        query $OPERATION_NAME(${'$'}dataKeys : [dataKey!]!) {
          $OPERATION_NAME(dataKeys: ${'$'}dataKeys) {
            data {
              dataKey
              error
              errorMsg
              showWidget
              data {
                metrics {
                  error
                  errMsg
                  summary {
                    title
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

    override fun getOperationNameList(): List<String> {
        return listOf(OPERATION_NAME)
    }

    override fun getQuery(): String = QUERY

    override fun getTopOperationName(): String = OPERATION_NAME
}