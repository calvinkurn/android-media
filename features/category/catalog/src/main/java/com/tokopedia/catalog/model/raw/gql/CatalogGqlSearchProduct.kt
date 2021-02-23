package com.tokopedia.catalog.model.raw.gql

const val CATALOG_GQL_SEARCH_PRODUCT: String = """query ace_search_product(${'$'}params: String!) {
  ace_search_product(params: ${'$'}params) {
    data {
      source
      shareURL
      q
      isFilter
      isQuerySafe
      suggestionText {
        text
        query
      }
      redirection {
        redirectURL
        redirectApplink
        departmentID
      }
      related {
        relatedKeyword
        position
        otherRelated {
          keyword
          url
          applink
          product {
            id
            name
            price
            image_url
            rating
            count_review
            url
            applink
            price_str
          }
        }
      }
      suggestions {
        suggestion
        totalData
      }
      suggestionInstead {
        suggestionInstead
        currentKeyword
        totalData
      }
      catalogs {
        id
        name
        price
        priceMin
        priceMax
        priceRaw
        priceMinRaw
        priceMaxRaw
        countProduct
        description
        imageURL
        url
        departmentID
      }
      products {
        id
        name
        childs
        url
        imageURL
        imageURL300
        imageURL500
        imageURL700
        priceRange
        price
        priceInt
        shop {
          id
          name
          url
          isGoldShop
          location
          city
          reputation
          clover
          isOfficial
          isPowerBadge
        }
        wholesalePrice {
          quantityMin
          quantityMax
          price
        }
        courierCount
        condition
        categoryID
        categoryName
        categoryBreadcrumb
        departmentID
        departmentName
        labels {
          title
          color
        }
        labelGroups {
          position
          title
          type
        }
        topLabel
        bottomLabel
        badges {
          title
          image_url
          show
        }
        freeOngkir {
          isActive
          imgURL
        }
        isFeatured
        rating
        countReview
        originalPrice
        discountExpiredTime
        discountStartTime
        discountPercentage
        sku
        stock
        gaKey
        isPreorder
        wishlist
        description
        minOrder
        isStockEmpty
        status
        warehouseIDDefault
      }
      profiles {
        id
        name
        avatar
        username
        bio
        thumbnails
        iskol
        followed
        isaffiliate
        following
        followers
        postCount
      }
      topProfile {
        id
        name
        avatar
        username
        bio
        thumbnails
        iskol
        followed
        isaffiliate
        following
        followers
        postCount
      }
      categories {
        id
        name
      }
      ticker {
        text
        query
      }
      autocompleteApplink
      defaultSearchURL
      navSource
      liteURL
      token
    }
    header {
      totalData
      totalDataText
      defaultView
      processTime
      additionalParams
      experiment
      suggestionInstead {
        suggestionInstead
        currentKeyword
        totalData
      }
      responseCode
      errorMessage
      keyword_process
    }
  }
}
"""