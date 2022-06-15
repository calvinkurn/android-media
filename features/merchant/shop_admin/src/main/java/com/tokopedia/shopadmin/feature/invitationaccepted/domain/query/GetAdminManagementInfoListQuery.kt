package com.tokopedia.shopadmin.feature.invitationaccepted.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.usecase.RequestParams

object GetAdminManagementInfoListQuery: GqlQueryInterface {

    private const val OPERATION_NAME = "GetAdminManagementInfoList"

    private const val SHOP_ID_KEY = "shop_id"

    private val ADMIN_MANAGEMENT_INFO_LIST_QUERY = """
        query $OPERATION_NAME(${'$'}shop_id: String!) {
          getAdminManagementInfoList(shop_id: ${'$'}shop_id, source: "admin-management-ui", lang: "id", device: "android") {
            allPermissionList {
              permissionId
              permissionName
              description
              iconURL
              permissionRecursive {
                permissionId
                permissionName
                description
                iconURL
              }
            }
          }
        }
    """.trimIndent()

    fun createRequestParams(shopId: String): Map<String, Any> {
        return RequestParams.create().apply {
            putString(SHOP_ID_KEY, shopId)
        }.parameters
    }

    override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)

    override fun getQuery(): String = ADMIN_MANAGEMENT_INFO_LIST_QUERY

    override fun getTopOperationName(): String = OPERATION_NAME
}