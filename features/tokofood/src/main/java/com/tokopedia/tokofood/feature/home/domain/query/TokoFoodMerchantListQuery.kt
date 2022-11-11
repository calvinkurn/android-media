package com.tokopedia.tokofood.feature.home.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

object TokoFoodMerchantListQuery: GqlQueryInterface {

    private const val OPERATION_NAME = "getMerchantList"
    private val QUERY = """
        query $OPERATION_NAME(${'$'}latlong: String!, ${'$'}timezone: String, ${'$'}filter: tokofoodParamFilterMerchantList, ${'$'}sortBy: tokofoodParamSort, ${'$'}pageKey: String, ${'$'}limit: Int) {
          tokofoodGetMerchantList(latlong: ${'$'}latlong, timezone: ${'$'}timezone, filter: ${'$'}filter, sortBy: ${'$'}sortBy, pageKey: ${'$'}pageKey, limit: ${'$'}limit) {
            nextPageKey
            merchants {
              id
              applink
              brandID
              name
              imageURL
              priceLevel {
                icon
                fareCount
              }
              merchantCategories
              rating
              ratingFmt
              distance
              distanceFmt
              etaFmt
              promo
              hasBranch
              branchApplink
              isClosed
              addressLocality
              additionalData {
                topTextBanner
                discountIcon
              }
            }
          }
        }
    """.trimIndent()

    override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)
    override fun getQuery(): String = QUERY
    override fun getTopOperationName(): String = OPERATION_NAME

}
