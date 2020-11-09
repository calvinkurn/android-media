package com.tokopedia.common.travel.data

/**
 * @author by furqan on 03/11/2020
 */
object TravelBannerGQLQuery {
    val QUERY_COLLECTIVE_BANNER = """
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
    """.trimIndent()
}