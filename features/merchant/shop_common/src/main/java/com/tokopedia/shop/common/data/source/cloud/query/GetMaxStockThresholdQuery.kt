package com.tokopedia.shop.common.data.source.cloud.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

internal object GetMaxStockThresholdQuery: GqlQueryInterface {

    const val PARAM_SHOP_ID = "shopID"

    private const val OPERATION_NAME = "GetIMSMeta"

    override fun getOperationNameList(): List<String> {
        return listOf(OPERATION_NAME)
    }

    override fun getQuery(): String {
        return """
            query $OPERATION_NAME(
               ${'$'}$PARAM_SHOP_ID : String!
            ) {
                $OPERATION_NAME(shopID:${'$'}$PARAM_SHOP_ID){
                  header {
                    process_time
                    messages
                    reason
                    error_code
                  }
                  data {
                    max_stock_threshold
                  }
                }
            }
        """.trimIndent()
    }

    override fun getTopOperationName(): String {
        return OPERATION_NAME
    }
}