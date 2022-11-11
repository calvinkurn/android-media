package com.tokopedia.similarsearch.getsimilarproducts

private const val product_id = "\$product_id"
private const val params = "\$params"

private fun getProductInfoGQLQuery() = """
    id
    name
    url
    image_url
    rating_average
    price
    shop {
     id
     is_gold_shop
     location
     city
     reputation
     clover
     is_official
     name
    }
    badges {
      image_url
    }
    label_groups {
      title
      position
      type
      url
    }
    label_group_variant {
        title
        type
        type_variant
        hex_color
    }
    category_id
    category_name
    rating
    count_review
    original_price
    discount_expired_time
    discount_start_time
    discount_percentage
    wishlist
    min_order
    free_ongkir {
      is_active
      img_url
    }
    applink
    price_range
""".trimIndent().replace("\n", " ")

internal fun getSimilarProductsGQLQuery() = """
query similar_products_image_search($product_id: String!, $params: String!) {
  similar_products_image_search(product_id: $product_id, params: $params) {
    data {
      products {
        ${getProductInfoGQLQuery()}
      }
      originalProduct {
        ${getProductInfoGQLQuery()}
      }
    }
  }
}
""".trimIndent().replace("\n", "")
