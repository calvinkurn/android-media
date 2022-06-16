package com.tokopedia.sellerhome.domain.gqlquery

import com.tokopedia.gql_query_annotation.GqlQueryInterface

/**
 * Created by @ilhamsuaib on 30/03/22.
 */

object GqlGetShopLocation : GqlQueryInterface {

    private const val OPERATION_NAME = "shopInfoByID"
    private val QUERY = """
        query $OPERATION_NAME(${'$'}shopID: Int!) {
          $OPERATION_NAME(input: {shopIDs: [${'$'}shopID], fields: ["other-shiploc"]}) {
            result {
              shippingLoc {
                provinceID
              }
            }
          }
        }
    """.trimIndent()

    override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)

    override fun getQuery(): String = QUERY

    override fun getTopOperationName(): String = OPERATION_NAME
}