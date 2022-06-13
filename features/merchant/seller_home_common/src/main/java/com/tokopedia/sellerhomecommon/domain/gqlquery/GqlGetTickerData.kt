package com.tokopedia.sellerhomecommon.domain.gqlquery

import com.tokopedia.gql_query_annotation.GqlQueryInterface

/**
 * Created by @ilhamsuaib on 17/01/22.
 */

object GqlGetTickerData : GqlQueryInterface {

    private const val OPERATION_NAME = "getTicker"
    val QUERY = """
            query $OPERATION_NAME(${'$'}page: String!) {
              ticker {
                tickers(page: ${'$'}page) {
                  id
                  title
                  ticker_type
                  message
                }
              }
            }
        """.trimIndent()

    override fun getOperationNameList(): List<String> {
        return listOf(OPERATION_NAME)
    }

    override fun getQuery(): String = QUERY

    override fun getTopOperationName(): String = ""
}