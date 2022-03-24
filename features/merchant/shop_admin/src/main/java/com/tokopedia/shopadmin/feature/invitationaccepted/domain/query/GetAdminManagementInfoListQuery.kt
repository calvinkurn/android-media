package com.tokopedia.shopadmin.feature.invitationaccepted.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

object GetAdminManagementInfoListQuery: GqlQueryInterface {

    private const val OPERATION_NAME = "getAdminInfo"
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

    override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)

    override fun getQuery(): String = GQL_QUERY

    override fun getTopOperationName(): String = OPERATION_NAME
}