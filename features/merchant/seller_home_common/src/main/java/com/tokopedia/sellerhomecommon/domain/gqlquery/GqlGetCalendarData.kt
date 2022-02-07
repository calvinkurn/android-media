package com.tokopedia.sellerhomecommon.domain.gqlquery

import com.tokopedia.gql_query_annotation.GqlQueryInterface

/**
 * Created by @ilhamsuaib on 07/02/22.
 */

object GqlGetCalendarData : GqlQueryInterface {

    private const val OPERATION_NAME = "fetchCalendarWidgetData"
    val GQL_QUERY = """
        query ${OPERATION_NAME}(${'$'}dataKeys: [dataKey!]!) {
          ${OPERATION_NAME}(dataKeys: ${'$'}dataKeys) {
            data{
              dataKey
              events {
                eventName
                description
                label
                startDate
                endDate
                url
                applink
              }
              emptyState {
                imageUrl
                title
                description
              }
              error
              errorMsg
              showWidget
            }
          }
        }
    """.trimIndent()

    override fun getOperationNameList(): List<String> {
        return listOf(OPERATION_NAME)
    }

    override fun getQuery(): String = GQL_QUERY

    override fun getTopOperationName(): String = OPERATION_NAME
}