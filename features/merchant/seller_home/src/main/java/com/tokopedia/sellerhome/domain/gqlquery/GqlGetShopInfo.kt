package com.tokopedia.sellerhome.domain.gqlquery

import com.tokopedia.gql_query_annotation.GqlQueryInterface

/**
 * Created by @ilhamsuaib on 30/03/22.
 */

object GqlGetShopInfo : GqlQueryInterface{

    private const val TOP_OPERATION_NAME = "getShopInfoMoengage"
    private const val OPERATION_NAME = "shopInfoMoengage"
    private val QUERY = """
        query $TOP_OPERATION_NAME(${'$'}userID: Int!) {
          $OPERATION_NAME(userID: ${'$'}userID) {
            info {
              shop_name
              shop_avatar
            }
          }
        }
    """.trimIndent()

    override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)

    override fun getQuery(): String = QUERY

    override fun getTopOperationName(): String = TOP_OPERATION_NAME

}