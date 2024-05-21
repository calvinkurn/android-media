package com.tokopedia.home.beranda.domain.gql.query

import com.tokopedia.gql_query_annotation.GqlQuery

@GqlQuery(GetHomeRecommendationCard.NAME, GetHomeRecommendationCard.QUERY)
internal object GetHomeRecommendationCard {
    const val NAME = "GetHomeRecommendationCardQuery"
    const val QUERY = """
        query GetHomeRecommendationCard(${'$'}productPage: Int!, ${'$'}layouts: String!, ${'$'}param: String!, ${'$'}location: String!, , ${'$'}productCardVersion: String!) {
            getHomeRecommendationCard(productPage: ${'$'}productPage, layouts: ${'$'}layouts, param: ${'$'}param, location: ${'$'}location, productCardVersion: ${'$'}productCardVersion) {
                pageNamee
                layoutName
                hasNextPage
                cards {
                  id
                  categoryID
                  layout
                  layoutTracker
                  dataStringJson
                  gradientColor
                  url
                  name
                  subtitle
                  price
                  rating
                  applink
                  clickUrl
                  imageUrl
                  iconUrl
                  isTopads
                  priceInt
                  clusterID
                  productKey
                  isWishlist
                  wishlistUrl
                  countReview
                  slashedPrice
                  ratingAverage
                  trackerImageUrl
                  slashedPriceInt
                  discountPercentage
                  recommendationType
                  categoryBreadcrumbs
                  label {
                    imageUrl
                    title
                    textColor
                    backColor
                  }
                  shop {
                    id
                    url
                    city
                    name
                    domain
                    applink
                    imageUrl
                    reputation
                  }
                  badges {
                    title
                    imageUrl
                  }
                  freeOngkir {
                    isActive
                    imageUrl
                  }
                  labelGroup {
                    url
                    type
                    title
                    position
                    styles {
                     key
                     value
                    }
                  }
                }
          }
        }
    """
}
