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

    val CATEGORY_LIST = """
        query {
          travelCategoryList() {
            category{
              product
              attributes{
                title
                webURL
                appURL
                imageURL
              }
            }
          }
        }
    """.trimIndent()

    val HOMEPAGE_DESTINATION = """
        query {
          TravelDestination() {
            destination{
              attributes{
                title
                subtitle
                webURL
                appURL
                imageURL
                }
            }
            meta{
              title
            }
          }
        }
    """.trimIndent()

    val DYNAMIC_SUBHOMEPAGE = """
        query dynamic(${'$'}dataType:TravelUnifiedSubhomepageDataType!,${'$'}widgetType: TravelUnifiedSubhomepageWidgetType!,${'$'}data: TravelUnifiedSubhomepageRequestData!) {
          TravelGetDynamicSubhomepage(dataType:${'$'}dataType, widgetType:${'$'}widgetType, data:${'$'}data){
            id
            product
            promoCode
            description
            cityID
            title
            subtitle
            prefix
            prefixStyle
            value
            imageURL
            webURL
            appURL
          }
        }
    """.trimIndent()

    val LAYOUT_SUBHOMEPAGE = """
        query {
          travelLayoutSubhomepage {
            data{
              id
              dataType
              widgetType
              priority
              title
              subtitle
              appURL
              webURL
              metaText
            }
            meta{
              templateID
              templateName
            }

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

    val RECENT_SEARCHES = """
        query{
          TravelCollectiveRecentSearches(product:ALL){
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
              type
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