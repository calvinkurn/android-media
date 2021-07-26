package com.tokopedia.travelhomepage.common.util

object TravelHomepageGqlQuery {
    val DESTINATION_CITY_ARTICLE = """
        query TravelArticle(${'$'}cityID: Int!){
          TravelArticle(cityID:${'$'}cityID) {
            items {
              tag
              title
              publishedDate
              webURL
              appURL
              imageURL
            }
            meta {
              title
              URLTitle
              webURL
              appURL
            }
          }
        }
    """.trimIndent()

    val DESTINATION_CITY_DATA = """
        query travelcollective(${'$'}webURLs:String!) {
            TravelDestinationCityData(WebURL :${'$'}webURLs) {
              CityID
              CityName
           }
        }
    """.trimIndent()

    val DESTINATION_CITY_SUMMARY = """
        query TravelDestinationSummary(${'$'}cityID:String!) {
          TravelDestinationSummary(cityID:${'$'}cityID) {
            title
            images{
              id
              imageURL
            }
            description
          }
        }
    """.trimIndent()

    val ORDER_LIST = """
        query TravelCollectiveOrderList(${'$'}page: Int!,${'$'}perPage: Int!,${'$'}filterStatus: String!,${'$'}cityID: Int) {
          TravelCollectiveOrderList(product: ALL, page:${'$'}page, perPage:${'$'}perPage, filterStatus:${'$'}filterStatus, cityID:${'$'}cityID) {
              orders{
                product
                title
                subtitle
                prefix
                value
                webURL
                appURL
                imageURL
              }

              meta{
                title
                appURL
                webURL
              }
          }
        }
    """.trimIndent()

    val RECOMMENDATION = """
        query TravelCollectiveRecommendation(${'$'}product: TravelColletiveCategory!,${'$'}cityID: Int){
          TravelCollectiveRecommendation(product:${'$'}product, cityID:${'$'}cityID) {
            items {
              product
              title
              subtitle
              prefix
              prefixStyling
              value
              webURL
              appURL
              imageURL
            }
            meta {
              title
              webURL
              appURL
            }
          }
        }
    """.trimIndent()
}