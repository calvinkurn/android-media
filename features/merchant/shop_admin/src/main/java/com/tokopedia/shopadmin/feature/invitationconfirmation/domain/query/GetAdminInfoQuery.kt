package com.tokopedia.shopadmin.feature.invitationconfirmation.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

object GetAdminInfoQuery : GqlQueryInterface {

    private const val OPERATION_NAME = "getAdminInfo"

    private val GQL_QUERY = """
        query $OPERATION_NAME(${'$'}source: String!, ${'$'}shop_id: Int!) {
               getAdminType(source: ${'$'}source) {
                    admin_data {
                       status
                    }
               }
               getAdminInfo(source: ${'$'}source, shop_id: ${'$'}shop_id) {
                   admin_data {
                      shop_manage_id
                   }
              }
            }
    """.trimIndent()

    override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)

    override fun getQuery(): String = GQL_QUERY

    override fun getTopOperationName(): String = OPERATION_NAME

}