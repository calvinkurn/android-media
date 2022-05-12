package com.tokopedia.sellerhomecommon.domain.gqlquery

import com.tokopedia.gql_query_annotation.GqlQueryInterface

/**
 * Created by @ilhamsuaib on 17/01/22.
 */

object GqlGetLineGraphData : GqlQueryInterface {

    private const val TOP_OPERATION_NAME = ""
    private const val OPERATION_NAME = ""
    val QUERY = """
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

    override fun getOperationNameList(): List<String> {
        return listOf(OPERATION_NAME)
    }

    override fun getQuery(): String = QUERY

    override fun getTopOperationName(): String = TOP_OPERATION_NAME
}