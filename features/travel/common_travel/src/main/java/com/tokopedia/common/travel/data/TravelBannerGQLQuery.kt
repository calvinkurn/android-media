package com.tokopedia.common.travel.data

import com.tokopedia.common.travel.data.TravelBannerGQLQuery.QUERY_COLLECTIVE_BANNER
import com.tokopedia.gql_query_annotation.GqlQuery

/**
 * @author by furqan on 03/11/2020
 */
@GqlQuery("QueryTravelBanner", QUERY_COLLECTIVE_BANNER)
object TravelBannerGQLQuery {
    const val QUERY_COLLECTIVE_BANNER = """
        query travelCollectiveBanner(${'$'}product: TravelColletiveCategory!) {
          travelCollectiveBanner(product:${'$'}product, countryID: "ID") {
            banners{
              id
              product
              attributes{
                description
                webURL
                appURL
                imageURL
                promoCode
                }
            }
            meta{
              title
              webURL
              appURL
              label
            }
          }
        }
    """
}