package com.tokopedia.sellerhomecommon.domain.gqlquery

import com.tokopedia.gql_query_annotation.GqlQueryInterface

/**
 * Created by @ilhamsuaib on 17/01/22.
 */

object GqlGetAnnouncementData : GqlQueryInterface {

    private const val OPERATION_NAME = "fetchAnnouncementWidgetData"
    val QUERY = """
        query $OPERATION_NAME(${'$'}dataKeys: [dataKey!]!) {
          $OPERATION_NAME(dataKeys:${'$'}dataKeys) {
            data {
              dataKey
              errorMsg
              title
              showWidget
              subtitle
              imageUrl
              button {
                applink
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