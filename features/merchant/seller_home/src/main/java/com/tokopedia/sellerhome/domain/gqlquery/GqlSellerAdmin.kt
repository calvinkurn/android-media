package com.tokopedia.sellerhome.domain.gqlquery

import com.tokopedia.gql_query_annotation.GqlQueryInterface

/**
 * Created by @ilhamsuaib on 30/03/22.
 */

object GqlSellerAdmin : GqlQueryInterface {

    private const val OPERATION_NAME = "getAdminType"
    private val QUERY = """
        query $OPERATION_NAME(${'$'}source: String!) {
          $OPERATION_NAME(source: ${'$'}source) {
            shopID
            isMultiLocation
            admin_data {
              detail_information {
                admin_role_type {
                  is_shop_admin
                  is_location_admin
                  is_shop_owner
                }
              }
            }
          }
        }
    """.trimIndent()

    override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)

    override fun getQuery(): String = QUERY

    override fun getTopOperationName(): String = OPERATION_NAME
}