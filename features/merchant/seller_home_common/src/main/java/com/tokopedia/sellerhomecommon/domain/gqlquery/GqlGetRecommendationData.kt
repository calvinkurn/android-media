package com.tokopedia.sellerhomecommon.domain.gqlquery

import com.tokopedia.gql_query_annotation.GqlQueryInterface

/**
 * Created by @ilhamsuaib on 17/01/22.
 */

object GqlGetRecommendationData : GqlQueryInterface {

    private const val OPERATION_NAME = "fetchRecommendationWidgetData"
    val QUERY = """
        query $OPERATION_NAME(${'$'}dataKeys: [dataKey!]!) {
          $OPERATION_NAME(dataKeys: ${'$'}dataKeys) {
            data {
              dataKey
              errorMsg
              showWidget
              data {
                ticker {
                  type
                  text
                }
                progressBar1 {
                  show
                  text
                  bar {
                    value
                    maxValue
                  }
                }
                progressBar2 {
                  show
                  text
                  bar {
                    value
                    maxValue
                    valueDisplay
                  }
                }
                recommendation {
                  title
                  list {
                    text
                    applink
                    type
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
