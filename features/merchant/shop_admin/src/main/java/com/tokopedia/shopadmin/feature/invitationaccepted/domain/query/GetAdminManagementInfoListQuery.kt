package com.tokopedia.shopadmin.feature.invitationaccepted.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

object GetAdminManagementInfoListQuery: GqlQueryInterface {

    private const val OPERATION_NAME = "getAdminManagementInfoList"
    private val GQL_QUERY = """
        query $OPERATION_NAME(${'$'}source: String!, ${'$'}lang: String!, ${'$'}device: String!, ${'$'}shop_id: String!) {
              $OPERATION_NAME(source: ${'$'}source, lang: ${'$'}lang, device: ${'$'}device, shop_id: ${'$'}shop_id) {
                  allPermissionList {               
                    permissionName
                    iconURL
                  }
                }
            }
    """.trimIndent()

    override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)

    override fun getQuery(): String = GQL_QUERY

    override fun getTopOperationName(): String = OPERATION_NAME
}