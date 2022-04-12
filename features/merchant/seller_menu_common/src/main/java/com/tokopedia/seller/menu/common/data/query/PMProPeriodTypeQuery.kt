package com.tokopedia.seller.menu.common.data.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

object PMProPeriodTypeQuery: GqlQueryInterface {

    private const val OPERATION_NAME = "GetUserShop"

    private const val SHOP_ID_KEY = "shopId"

    @JvmStatic
    fun createRequestParams(shopId: Long) = HashMap<String, Any>().apply {
        put(SHOP_ID_KEY, shopId)
    }

    override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)

    override fun getQuery(): String = """
            query $OPERATION_NAME(${'$'}shopId: Int!) {
              goldGetPMSettingInfo(shopID: ${'$'}shopId, source: "goldmerchant") {
                period_type_pm_pro
              }
            }
        """.trimIndent()

    override fun getTopOperationName(): String = OPERATION_NAME

}