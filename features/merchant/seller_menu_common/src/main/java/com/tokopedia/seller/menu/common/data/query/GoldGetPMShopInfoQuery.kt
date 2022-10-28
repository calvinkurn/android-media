package com.tokopedia.seller.menu.common.data.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

object GoldGetPMShopInfoQuery: GqlQueryInterface {

    private const val OPERATION_NAME = "GoldGetPMShopInfo"

    private const val SHOP_ID_KEY = "shopId"
    private const val KEY_FILTER = "filter"
    private const val KEY_INCLUDING_PM_PRO_ELIGIBILITY = "including_pm_pro_eligibility"

    fun createRequestParams(shopId: Long) = HashMap<String, Any>().apply {
        val filter: Map<String, Boolean> = mapOf(
            KEY_INCLUDING_PM_PRO_ELIGIBILITY to true
        )
        put(SHOP_ID_KEY, shopId)
        put(KEY_FILTER, filter)
    }

    override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)

    override fun getQuery(): String = """
            query $OPERATION_NAME(${'$'}shopId: Int!, ${'$'}filter: GetPMShopInfoFilter) {
              goldGetPMShopInfo(shop_id: ${'$'}shopId, source: "android-goldmerchant", filter: ${'$'}filter) {
                kyc_status_id
                is_eligible_pm
                is_eligible_pm_pro
              }
            }
        """.trimIndent()


    override fun getTopOperationName(): String = OPERATION_NAME

}