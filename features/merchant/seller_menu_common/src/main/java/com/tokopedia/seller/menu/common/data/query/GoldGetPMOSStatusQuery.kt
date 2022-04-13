package com.tokopedia.seller.menu.common.data.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

object GoldGetPMOSStatusQuery: GqlQueryInterface {

    private const val OPERATION_NAME = "ShopInfoById"

    private const val SHOP_ID_KEY = "shopId"

    fun createRequestParams(shopId: Long) = HashMap<String, Any>().apply {
        put(SHOP_ID_KEY, shopId)
    }

    override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)

    override fun getQuery(): String = """
            query $OPERATION_NAME(${'$'}shopId: Int!) {
                goldGetPMOSStatus(
                    shopID: ${'$'}shopId,
                    includeOS: true){
                        data {
                            power_merchant {
                                status
                                pm_tier
                            }
                            official_store {
                                status
                                error
                            }
                    }
               }
            }
        """.trimIndent()


    override fun getTopOperationName(): String = OPERATION_NAME

}