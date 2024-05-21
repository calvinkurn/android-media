package com.tokopedia.libra.data.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

object LibraQuery : GqlQueryInterface {

    private const val OPERATION_NAME = "getHomeLibraParameters"

    override fun getOperationNameList() = listOf(OPERATION_NAME)

    override fun getTopOperationName() = OPERATION_NAME

    override fun getQuery() =
        """
            query $OPERATION_NAME(${'$'}type: String!) {
                $OPERATION_NAME(type: ${'$'}type)
            }
        """.trimIndent()
}
