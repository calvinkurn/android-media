package com.tokopedia.shopadmin.feature.invitationaccepted.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.usecase.RequestParams

object GetAdminManagementInfoListQuery: GqlQueryInterface {

    private const val OPERATION_NAME = "getAdminInfo"

    private const val SHOP_ID_KEY = "shop_id"
    private const val SOURCE_KEY = "source"
    private const val SOURCE = "admin-info-permission-android"

    private val GQL_QUERY = """
        query $OPERATION_NAME(${'$'}source: String!, ${'$'}shop_id: Int!) {
              $OPERATION_NAME(source: ${'$'}source, shop_id: ${'$'}shop_id) {
                    admin_data {
                      permission_list {  
                          permission_name
                          icon_url
                      }
                   }
                }
            }
    """.trimIndent()

    fun createRequestParams(shopId: Long): Map<String, Any> {
        return RequestParams.create().apply {
            putLong(SHOP_ID_KEY, shopId)
            putString(SOURCE_KEY, SOURCE)
        }.parameters
    }


    override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)

    override fun getQuery(): String = GQL_QUERY

    override fun getTopOperationName(): String = OPERATION_NAME
}