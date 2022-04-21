package com.tokopedia.sellerhome.domain.gqlquery

import com.tokopedia.gql_query_annotation.GqlQueryInterface

/**
 * Created by @ilhamsuaib on 30/03/22.
 */

object GqlGetShopInfoById : GqlQueryInterface {

    private const val OPERATION_NAME = "shopInfoByID"
    private val QUERY = """
        query $OPERATION_NAME(${'$'}shopID: Int!) {
          $OPERATION_NAME(input: {shopIDs: [${'$'}shopID], fields: ["core","closed_info","shop-snippet","status"]}) {
            result {
              shopCore{
                url
              }
              closedInfo {
                detail {
                  startDate
                  endDate
                  status
                }
              }
              statusInfo {
                shopStatus
              }
              shopSnippetURL
            }
            error {
              message
            }
          }
        }
    """.trimIndent()

    override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)

    override fun getQuery(): String = QUERY

    override fun getTopOperationName(): String = OPERATION_NAME
}