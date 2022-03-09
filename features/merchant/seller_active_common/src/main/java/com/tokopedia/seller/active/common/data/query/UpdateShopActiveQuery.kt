package com.tokopedia.seller.active.common.data.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

object UpdateShopActiveQuery : GqlQueryInterface {

    private const val OPERATION_NAME = "updateShopActive"
    const val QUERY = "mutation updateShopActive(\$input: ParamUpdateLastActive!){\n" +
            "  updateShopActive(input: \$input) {\n" +
            "    success\n" +
            "    message\n" +
            "  }\n" +
            "}"

    override fun getOperationNameList(): List<String> {
        return listOf(OPERATION_NAME)
    }

    override fun getQuery(): String = QUERY

    override fun getTopOperationName(): String = OPERATION_NAME
}