package com.tokopedia.common.travel.data

object CommonTravelGqlQuery {
    var TRAVEL_COLLECTIVE_BANNER = """
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
            }
          }
        }
    """.trimIndent()
}