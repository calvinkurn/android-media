package com.tokopedia.sellerhomecommon.domain.gqlquery

import com.tokopedia.gql_query_annotation.GqlQueryInterface

/**
 * Created by @ilhamsuaib on 17/01/22.
 */

object GqlGetPostData : GqlQueryInterface {

    private const val TOP_OPERATION_NAME = "getPostWidgetData"
    private const val OPERATION_NAME = "fetchPostWidgetData"
    val QUERY = """
        query $TOP_OPERATION_NAME(${'$'}dataKeys: [dataKey!]!) {
          $OPERATION_NAME(dataKeys: ${'$'}dataKeys) {
            data {
              datakey
              list {
                title
                url
                applink
                subtitle
                featuredMediaURL
                stateMediaURL
                stateText
                pinned
              }
              cta{
                text
                applink
              }
              error
              errorMsg
              showWidget
              emphasizeType
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
