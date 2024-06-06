package com.tokopedia.recommendation_widget_common.domain.request

internal class GetRecommendationUseCaseRequest {
    companion object {
        val widgetListQuery = """
            query productRecommendation(${'$'}userID: Int!, ${'$'}pageName: String!, ${'$'}pageNumber: Int!, ${'$'}xDevice: String!, ${'$'}xSource: String!, ${'$'}queryParam: String!, ${'$'}productIDs: String!, ${'$'}categoryIDs: String!, ${'$'}keywords: [String!]!, ${'$'}tokoNow: Boolean, ${'$'}productCardVersion: Int, ${'$'}currentSessionID: String!, ${'$'}refreshType: String!, ${'$'}enterFrom: String!, ${'$'}sourcePageType: String!) {
                  productRecommendationWidget(userID: ${'$'}userID, pageName: ${'$'}pageName, pageNumber: ${'$'}pageNumber, xDevice: ${'$'}xDevice, xSource: ${'$'}xSource, queryParam: ${'$'}queryParam, productIDs : ${'$'}productIDs, categoryIDs : ${'$'}categoryIDs, keywords: ${'$'}keywords, tokoNow : ${'$'}tokoNow, productCardVersion : ${'$'}productCardVersion, currentSessionID: ${'$'}currentSessionID, refreshType: ${'$'}refreshType, enterFrom: ${'$'}enterFrom, sourcePageType: ${'$'}sourcePageType) {
                    data {
                      tID
                      source
                      title
                      subtitle
                      foreignTitle
                      widgetUrl
                      pageName
                      seeMoreAppLink
                      layoutType
                      pagination {
                        currentPage
                        nextPage
                        prevPage
                        hasNext
                      }
                      campaign{
                        appLandingPageLink
                        landingPageLink
                        thematicID
                        assets {
                          banner{
                            apps
                          }
                        }
                      }
                      appLog {
                        sessionID
                        requestID
                        logID
                      }
                      recommendation {
                        id
                        name
                        categoryBreadcrumbs
                        url
                        appUrl
                        clickUrl
                        wishlistUrl
                        trackerImageUrl
                        imageUrl
                        price
                        priceInt
                        discountPercentage
                        slashedPrice
                        slashedPriceInt
                        isWishlist
                        minOrder
                        maxOrder
                        shop {
                          id
                          name
                          city
                          isGold
                          isOfficial
                        }
                        departmentId
                        labels {
                          title
                          color
                        }
                        labelgroup{
                            position
                            title
                            type
                            url
                            styles {
                              key
                              value
                            }
                        }
                        badges {
                          title
                          imageUrl
                        }
                        wholesalePrice {
                          price
                          quantityMax
                          quantityMin
                          priceString
                        }
                        freeOngkir{
                          isActive
                          imageUrl
                        }
                        rating
                        countReview
                        ratingAverage
                        recommendationType
                        stock
                        isTopads
                        specificationLabels {
                            key
                            value
                        }
                        parentID
                        recParam
                        adsLog {
                          creativeID
                          logExtra
                        }
                        countSold
                      }
                    }
                  }
                }
        """.trimIndent()

        val singleQuery = """
            query productRecommendationSingle(${'$'}pageNumber: Int!, ${'$'}pageName: String!, ${'$'}productIDs: String!, ${'$'}queryParam: String!, ${'$'}productCardVersion: Int, ${'$'}currentSessionID: String!, ${'$'}refreshType: String!, ${'$'}enterFrom: String!, ${'$'}sourcePageType: String!) {
                productRecommendationWidgetSingle(pageNumber: ${'$'}pageNumber, pageName: ${'$'}pageName, productIDs: ${'$'}productIDs, queryParam: ${'$'}queryParam, productCardVersion : ${'$'}productCardVersion, currentSessionID: ${'$'}currentSessionID, refreshType: ${'$'}refreshType, enterFrom: ${'$'}enterFrom, sourcePageType: ${'$'}sourcePageType){
                   data {
                       tID
                       source
                       title
                       foreignTitle
                       widgetUrl
                       seeMoreAppLink
                       seeMoreUrlLink
                       pageName
                       pagination{
                            hasNext
                       }
                       campaign{
                        appLandingPageLink
                        landingPageLink
                        assets {
                          banner{
                            apps
                          }
                        }
                       }
                       appLog {
                         sessionID
                         requestID
                         logID
                       }
                       recommendation {
                           id
                           name
                           categoryBreadcrumbs
                           url
                           appUrl
                           clickUrl
                           wishlistUrl
                           trackerImageUrl
                           imageUrl
                           relatedProductAppLink
                           relatedProductUrlLink
                           price
                           priceInt
                           discountPercentage
                           slashedPrice
                           slashedPriceInt
                           shop {
                               id
                               name
                               city
                           }
                          departmentId
                          labels {
                              title
                              color
                          }
                          labelgroup{
                            position
                            title
                            type
                            url
                            styles {
                              key
                              value
                            }
                          }
                          badges {
                              title
                              imageUrl
                          }
                          wholesalePrice {
                              price
                              quantityMax
                              quantityMin
                              priceString
                          }
                          freeOngkir{
                            isActive
                            imageUrl
                          }
                          rating
                          ratingAverage
                          countReview
                          recommendationType
                          stock
                          isTopads
                          specificationLabels {
                            key
                            value
                        }
                        parentID
                        recParam
                        adsLog {
                           creativeID
                           logExtra
                        }
                        countSold
                       }
                   }
                 }
             }
        """.trimIndent()
    }
}
