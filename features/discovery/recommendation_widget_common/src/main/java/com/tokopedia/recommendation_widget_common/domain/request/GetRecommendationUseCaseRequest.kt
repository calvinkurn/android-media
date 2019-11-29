package com.tokopedia.recommendation_widget_common.domain.request

internal class GetRecommendationUseCaseRequest {
    companion object {
        val widgetListQuery = """
            query productRecommendation(${'$'}userID: Int!, ${'$'}pageName: String!, ${'$'}pageNumber: Int!, ${'$'}xDevice: String!, ${'$'}xSource: String!, ${'$'}queryParam: String!, ${'$'}productIDs: String!) {
                  productRecommendationWidget(userID: ${'$'}userID, pageName: ${'$'}pageName, pageNumber: ${'$'}pageNumber, xDevice: ${'$'}xDevice, xSource: ${'$'}xSource, queryParam: ${'$'}queryParam, productIDs : ${'$'}productIDs) {
                    data {
                      tID
                      source
                      title
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
                        }
                        departmentId
                        labels {
                          title
                          color
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
                        recommendationType
                        stock
                        isTopads
                      }
                    }
                  }
                }

    """.trimIndent()

        val singleQuery = """
            query productRecommendationSingle(${'$'}pageNumber: Int!, ${'$'}${'$'}pageName: String!, ${'$'}productIDs: String!) {
                productRecommendationWidgetSingle(pageNumber: ${'$'}pageNumber, pageName: ${'$'}pageName, productIDs: ${'$'}productIDs){
                    meta {
                        recommendation
                        size
                        failSize
                        processTime
                        experimentVersion
                    }
                   data {
                       tID
                       source
                       title
                       foreignTitle
                       widgetUrl
                       seeMoreAppLink
                       seeMoreUrlLink
                       pageName
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
                          rating
                          countReview
                          recommendationType
                          stock
                          isTopads
                       }
                   }
                 }
             }

    """.trimIndent()
    }
}