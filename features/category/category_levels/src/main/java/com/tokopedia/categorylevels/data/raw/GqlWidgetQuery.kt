package com.tokopedia.categorylevels.data.raw

const val GQL_WIDGET_QUERY: String = """query widgetQuery(
  ${'$'}xDevice: String!, 
  ${'$'}categoryIDs: String!,
  ${'$'}pageName: String!,
  ${'$'}xSource: String!, 
  ${'$'}queryParam: String!,
  ${'$'}pageNumber: Int
){ 
    productRecommendationWidget(
      xDevice: ${'$'}xDevice, 
      categoryIDs: ${'$'}categoryIDs,
      pageName: ${'$'}pageName,
      xSource: ${'$'}xSource, 
      queryParam: ${'$'}queryParam, 
      pageNumber: ${'$'}pageNumber
    ) { 
    meta { 
      recommendation 
      size 
      failSize 
      processTime 
      experimentVersion 
    } 
    data { 
      tID
      layoutType
      title 
      foreignTitle 
      pageName 
      recommendation { 
        id 
        name 
        price 
        priceInt 
        rating
        departmentId 
        badges { 
          title 
          imageUrl 
        } 
        url
        urlPath
        isTopads
        recommendationType 
        countReview
        countReviewFloat
        shop {
          id
          name
          city
        }
        recommendationType
      } 
    } 
  } 
}"""