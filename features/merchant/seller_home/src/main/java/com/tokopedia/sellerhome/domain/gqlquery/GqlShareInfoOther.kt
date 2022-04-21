package com.tokopedia.sellerhome.domain.gqlquery

import com.tokopedia.gql_query_annotation.GqlQueryInterface

/**
 * Created by @ilhamsuaib on 30/03/22.
 */

object GqlShareInfoOther : GqlQueryInterface {

    private const val TOP_OPERATION_NAME = "GetUserShop"
    private const val OPERATION_NAME = "shopInfoByID"
    private val QUERY = """
        query $TOP_OPERATION_NAME(${'$'}shopId: Int!) {
          $OPERATION_NAME(
            input: {
              shopIDs: [${'$'}shopId]
              fields: ["shop-snippet", "location", "core", "branch-link"]
            }
          ) {
            result {
              shopSnippetURL
              location
              branchLinkDomain
              shopCore {
                description
                tagLine
                url
              }
            }
          }
        }
    """.trimIndent()

    override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)

    override fun getQuery(): String = QUERY

    override fun getTopOperationName(): String = TOP_OPERATION_NAME
}