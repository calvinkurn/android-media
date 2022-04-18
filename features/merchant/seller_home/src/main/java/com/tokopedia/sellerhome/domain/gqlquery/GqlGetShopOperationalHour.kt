package com.tokopedia.sellerhome.domain.gqlquery

import com.tokopedia.gql_query_annotation.GqlQueryInterface

/**
 * Created by @ilhamsuaib on 30/03/22.
 */

object GqlGetShopOperationalHour : GqlQueryInterface {

    private const val OPERATION_NAME = "getShopOperationalHourStatus"
    private val QUERY = """
        query $OPERATION_NAME(${'$'}shopID: String!, ${'$'}type: Int!) {
          $OPERATION_NAME(shopID: ${'$'}shopID, type: ${'$'}type) {
            statusActive
            startTime
            endTime
            error {
              message
            }
          }
        }
    """.trimIndent()

    override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)

    override fun getQuery(): String = QUERY

    override fun getTopOperationName(): String = OPERATION_NAME
}