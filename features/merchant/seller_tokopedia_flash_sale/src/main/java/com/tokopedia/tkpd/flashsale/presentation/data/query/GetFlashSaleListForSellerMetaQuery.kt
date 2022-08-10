package com.tokopedia.tkpd.flashsale.presentation.data.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

object GetFlashSaleListForSellerMetaQuery : GqlQueryInterface {

    private const val OPERATION_NAME = "getFlashSaleListForSellerMeta"
    private val QUERY = """
       query $OPERATION_NAME(${'$'}params: GetFlashSaleListForSellerMeta!) {
         $OPERATION_NAME(params: ${'$'}params) {
           tab_list {
             tab_id
             tab_name
             total_campaign
             display_name
           }
         }
       }

    """.trimIndent()

    override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)
    override fun getQuery(): String = QUERY
    override fun getTopOperationName(): String = OPERATION_NAME
}