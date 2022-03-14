package com.tokopedia.seller.active.common.data.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

object UpdateShopActiveQuery : GqlQueryInterface {

    private const val OPERATION_NAME = "updateShopActive"

    override fun getOperationNameList(): List<String> {
        return listOf(OPERATION_NAME)
    }

    override fun getQuery(): String = """
        mutation $OPERATION_NAME(${'$'}input: ParamUpdateLastActive!){
          $OPERATION_NAME(input: ${'$'}input){
            success
            message
          }
        }
    """.trimIndent()

    override fun getTopOperationName(): String = OPERATION_NAME
}