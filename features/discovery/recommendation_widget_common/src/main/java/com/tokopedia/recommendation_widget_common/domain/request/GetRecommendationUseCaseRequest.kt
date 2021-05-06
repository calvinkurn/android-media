package com.tokopedia.recommendation_widget_common.domain.request

internal class GetRecommendationUseCaseRequest {
    companion object {
        val widgetListQuery = """
            query productRecommendation(${'$'}userID: Int!, ${'$'}pageName: String!, ${'$'}pageNumber: Int!, ${'$'}xDevice: String!, ${'$'}xSource: String!, ${'$'}queryParam: String!, ${'$'}productIDs: String!, ${'$'}categoryIDs: String!) {
                  productRecommendationWidget(userID: ${'$'}userID, pageName: ${'$'}pageName, pageNumber: ${'$'}pageNumber, xDevice: ${'$'}xDevice, xSource: ${'$'}xSource, queryParam: ${'$'}queryParam, productIDs : ${'$'}productIDs, categoryIDs : ${'$'}categoryIDs) {
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
                      }
                    }
                  }
                }

    """.trimIndent()

        val singleQuery = """
            query productRecommendationSingle(${'$'}pageNumber: Int!, ${'$'}pageName: String!, ${'$'}productIDs: String!, ${'$'}queryParam: String!) {
                productRecommendationWidgetSingle(pageNumber: ${'$'}pageNumber, pageName: ${'$'}pageName, productIDs: ${'$'}productIDs, queryParam: ${'$'}queryParam){
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
                       }
                   }
                 }
             }

    """.trimIndent()
    }
}