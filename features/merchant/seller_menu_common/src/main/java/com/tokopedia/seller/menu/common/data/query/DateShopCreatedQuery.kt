package com.tokopedia.seller.menu.common.data.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

object DateShopCreatedQuery: GqlQueryInterface {

    private const val OPERATION_NAME = "DateShopCreated"

    override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)

    override fun getQuery(): String = """
            query $OPERATION_NAME {
              userShopInfo {
                info {
                  date_shop_created
                }
              }
            }
        """.trimIndent()

    override fun getTopOperationName(): String = OPERATION_NAME

}