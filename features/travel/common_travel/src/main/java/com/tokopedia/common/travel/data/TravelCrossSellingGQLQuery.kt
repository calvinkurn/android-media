package com.tokopedia.common.travel.data

/**
 * @author by furqan on 13/11/2020
 */
object TravelCrossSellingGQLQuery {
    val QUERY_CROSS_SELLING = """
        query crossSellQuery(${'$'}orderID : String!,${'$'}orderCategory:CrossSellOrderCategory!) {
          crossSell(orderID:${'$'}orderID, orderCategory:${'$'}orderCategory) {
              items {
                product
                title
                content
                prefix
                uri
                uriWeb
                imageURL
                value
              }
              meta {
                title
                uri
                uriWeb
              }
          }
        }
    """.trimIndent()
}