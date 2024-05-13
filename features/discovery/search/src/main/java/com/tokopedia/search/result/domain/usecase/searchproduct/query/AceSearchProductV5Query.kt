package com.tokopedia.search.result.domain.usecase.searchproduct.query

import com.tokopedia.gql_query_annotation.GqlQuery

@GqlQuery(
    AceSearchProductV5Query.QUERY_NAME,
    AceSearchProductV5Query.ACE_SEARCH_PRODUCT_V5_QUERY
)
object AceSearchProductV5Query {

    const val QUERY_NAME = "AceSearchProductV5"

    const val ACE_SEARCH_PRODUCT_V5_QUERY = """
        query SearchProductQueryV5(${'$'}params: String!) {
          searchProductV5(params: ${'$'}params) {
            header {
              totalData
              responseCode
              keywordProcess
              keywordIntention
              componentID
              meta {
                productListType
                hasPostProcessing
                hasButtonATC
                dynamicFields
              }
              isQuerySafe
              additionalParams
              autocompleteApplink
              backendFilters
              backendFiltersToggle
            }
            data {
              redirection {
                applink
              }
              ticker {
                id
                text
                query
                componentID
                trackingOption
              }
              related {
                relatedKeyword
                position
                trackingOption
                otherRelated {
                  keyword
                  url
                  applink
                  componentID
                  products {
                    id
                    name
                    url
                    applink
                    mediaURL {
                      image
                    }
                    shop {
                      id
                      name
                      city
                      tier
                    }
                    badge {
                      id
                      title
                      url
                    }
                    price {
                      text
                      number
                    }
                    freeShipping {
                      url
                    }
                    labelGroups {
                      id
                      position
                      title
                      type
                      url
                      styles {
                        key
                        value
                      }
                    }
                    rating
                    wishlist
                    ads {
                      id
                      productClickURL
                      productViewURL
                      productWishlistURL
                      tag
                      creativeID
                      logExtra
                    }
                    meta {
                      warehouseID
                      componentID
                      isImageBlurred
                      parentID
                    }
                  }
                }
              }
              suggestion {
                suggestion
                query
                text
                componentID
                trackingOption
              }
              banner {
                position
                text
                applink
                imageURL
                componentID
                trackingOption
              }
              violation {
                headerText
                descriptionText
                imageURL
                ctaApplink
                buttonText
                buttonType
              }
              products {
                id
                name
                url
                applink
                mediaURL {
                  image
                  image300
                  image700
                  videoCustom
                }
                shop {
                  id
                  name
                  url
                  city
                  tier
                }
                badge {
                  id
                  title
                  url
                }
                price {
                  text
                  number
                  range
                  original
                  discountPercentage
                }
                freeShipping {
                  url
                }
                labelGroups {
                  id
                  position
                  title
                  type
                  url
                  styles {
                    key
                    value
                  }
                }
                labelGroupsVariant {
                  title
                  type
                  typeVariant
                  hexColor
                }
                category {
                  id
                  name
                  breadcrumb
                }
                rating
                wishlist
                ads {
                  id
                  productClickURL
                  productViewURL
                  productWishlistURL
                  tag
                  creativeID
                  logExtra
                }
                meta {
                  parentID
                  warehouseID
                  isPortrait
                  isImageBlurred
                }
              }
              totalDataText
            }
          }
        }
    """
}
