package com.tokopedia.shopadmin.feature.invitationconfirmation.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

object GetShopAdminInfoQuery: GqlQueryInterface {

    private const val OPERATION_NAME = "getShopAdminInfo"

    private val GQL_QUERY = """
        query $OPERATION_NAME {
           shop {
              shop_name
              logo
           }
        }
    """.trimIndent()

    override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)

    override fun getQuery(): String = GQL_QUERY

    override fun getTopOperationName(): String = OPERATION_NAME

}