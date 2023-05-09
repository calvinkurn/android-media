package com.tokopedia.topads.common.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

internal object GetShopInfoV1 : GqlQueryInterface {

    private const val OPERATION_NAME = "topAdsGetShopInfoV1_1"
    private const val PARAM_SHOP_ID = "shopID"
    private const val PARAM_SOURCE = "source"

    override fun getOperationNameList(): List<String> {
        return listOf(OPERATION_NAME)
    }

    override fun getQuery(): String {
        return """
            query $OPERATION_NAME(${'$'}$PARAM_SHOP_ID: String!, ${'$'}$PARAM_SOURCE: String!) {
                $OPERATION_NAME(shop_id: ${'$'}$PARAM_SHOP_ID, source: ${'$'}$PARAM_SOURCE) {
                    data {
                        category
                        category_desc
                    }
                }
            }
        """.trimIndent()
    }

    override fun getTopOperationName(): String {
        return OPERATION_NAME
    }
}
