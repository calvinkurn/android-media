package com.tokopedia.seller.menu.common.data.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

object ShopInfoByIdQuery: GqlQueryInterface {

    private const val OPERATION_NAME = "ShopInfoById"

    private const val SHOP_ID_KEY = "shopId"

    @JvmStatic
    fun createRequestParams(shopId: Long) = HashMap<String, Any>().apply {
        put(SHOP_ID_KEY, shopId)
    }

    override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)

    override fun getQuery(): String = """
            query ${OPERATION_NAME}(${'$'}shopId: Int!) {
              shopInfoByID(
                input: {
                  shopIDs: [${'$'}shopId]
                  fields: ["other-goldos", "shopstats-limited", "shop-snippet", "location", "core", "branch-link"]
                }
              ) {
                result {
                  goldOS {
                    title
                    badge
                    shopTier  
                    shopTierWording
                    shopGrade
                    shopGradeWording
                  }
                  statsByDate {
                    identifier
                    value
                    startTime
                  }
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

    override fun getTopOperationName(): String = OPERATION_NAME

}