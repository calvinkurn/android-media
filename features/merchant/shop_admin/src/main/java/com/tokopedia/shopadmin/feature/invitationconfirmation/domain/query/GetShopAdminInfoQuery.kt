package com.tokopedia.shopadmin.feature.invitationconfirmation.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.usecase.RequestParams

object GetShopAdminInfoQuery: GqlQueryInterface {

    private const val OPERATION_NAME = "getShopAdminInfo"
    private const val SHOP_ID_KEY = "shop_id"
    private const val SOURCE_KEY = "source"
    private const val SOURCE = "getShopAdminInfo-android"

    private val GQL_QUERY = """
        query ${OPERATION_NAME}(${'$'}source: String!, ${'$'}shop_id: Int!) {
           shop {
              shop_name
              logo
           }
           getAdminInfo(source: ${'$'}source, shop_id: ${'$'}shop_id) {
              admin_data {
                 shop_manage_id
              }
           }
        }
    """.trimIndent()

    fun createRequestParams(shopId: Long): Map<String, Any> {
        return RequestParams.create().apply {
            putString(SOURCE_KEY, SOURCE)
            putLong(SHOP_ID_KEY, shopId)
        }.parameters
    }

    override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)

    override fun getQuery(): String = GQL_QUERY

    override fun getTopOperationName(): String = OPERATION_NAME

}