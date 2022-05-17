package com.tokopedia.sellerhomecommon.domain.gqlquery

import com.tokopedia.gql_query_annotation.GqlQueryInterface

/**
 * Created by @ilhamsuaib on 25/04/22.
 */

object GqlGetShopInfoTicker : GqlQueryInterface {

    private const val OPERATION_NAME = "shopInfoByID"
    val QUERY = """
        query $OPERATION_NAME(${'$'}shopId: Int!) {
          $OPERATION_NAME(input: {shopIDs: [${'$'}shopId], fields: ["status"], domain: "", source: "sellerapp"}) {
            result {
              statusInfo {
                statusTitle
                statusMessage
                tickerType
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