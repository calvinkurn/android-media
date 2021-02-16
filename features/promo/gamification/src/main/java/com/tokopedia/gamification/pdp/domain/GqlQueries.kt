package com.tokopedia.gamification.pdp.domain

const val GAMI_PRODUCT_RECOM_WIDGET_QUERY = """
    query productRecommendation(${'$'}userID: Int!, ${'$'}pageName: String!, ${'$'}pageNumber: Int!, ${'$'}xSource: String!, ${'$'}queryParam: String!, ${'$'}shopId:Int) {
  productRecommendationWidget(userID: ${'$'}userID, pageName: ${'$'}pageName, pageNumber: ${'$'}pageNumber, xSource: ${'$'}xSource, queryParam: ${'$'}queryParam, shopId: ${'$'}shopId) {
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
        labelgroup{
            position
            title
            type
        }
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
        badges {
          imageUrl
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
      }
    }
  }
}
"""