package com.tokopedia.common_category.data.raw

const val GQL_NAV_SEARCH_PRODUCT: String = """query SearchProduct(${'$'}params: String) {
  searchProduct(params: ${'$'}params) {
    source
    totalData: count
    count_text
    additionalParams: additional_params
    errorMessage
    lite_url
    redirection {
      redirectionURL: redirect_url
      departmentID: department_id
    }
    suggestion {
      suggestion
      suggestionCount
      currentKeyword
      instead
      insteadCount
      suggestionText: text
      suggestionTextQuery: query
    }
    related {
      relatedKeyword: related_keyword
      otherRelated: other_related {
        keyword
        url
        applink
      }
    }
    isQuerySafe
    catalogs {
      id
      name
      price
      priceMin: price_min
      priceMax: price_max
      countProduct: count_product
      description
      imageURL: image_url
      url
      category: department_id
    }
    products {
      id
      name
      childs
      url
      imageURL: image_url
      imageURL300: image_url_300
      imageURL500: image_url_500
      imageURL700: image_url_700
      price
      priceRange: price_range
      category: department_id
      categoryID: category_id
      categoryName: category_name
      categoryBreadcrumb: category_breadcrumb
      discountPercentage: discount_percentage
      originalPrice: original_price
      shop {
        id
        name
        url
        isPowerBadge: is_power_badge
        isOfficial: is_official
        location
        city
        reputation
        clover
      }
      wholesalePrice: whole_sale_price {
        quantityMin: quantity_min
        quantityMax: quantity_max
        price
      }
      courierCount: courier_count
      condition
      labels {
        title
        color
      }
      labelGroups: label_groups {
        position
        type
        title
      }
      badges {
        title
        imageURL: image_url
        show
      }
      isFeatured: is_featured
      rating
      countReview: count_review
      stock
      GAKey: ga_key
      preorder: is_preorder
      wishlist
      free_ongkir{
        is_active
        img_url
      }
      shop {
        id
        name
        url
        goldmerchant: is_power_badge
        location
        city
        reputation
        clover
        official: is_official
      }
    }
  }
}"""