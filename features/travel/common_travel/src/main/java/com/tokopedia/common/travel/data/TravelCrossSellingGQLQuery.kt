package com.tokopedia.common.travel.data

import com.tokopedia.common.travel.data.TravelCrossSellingGQLQuery.QUERY_CROSS_SELLING
import com.tokopedia.gql_query_annotation.GqlQuery

/**
 * @author by furqan on 13/11/2020
 */

@GqlQuery("QueryTravelCrossSelling", QUERY_CROSS_SELLING)
object TravelCrossSellingGQLQuery {
    const val QUERY_CROSS_SELLING = """
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
    """
}