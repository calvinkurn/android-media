package com.tokopedia.similarsearch

private const val product_id = "\$product_id"
private const val params = "\$params"

private fun getProductInfoGQLQuery() = """
    id
    name
    url
    image_url
    image_url_700
    price
    shop {
     id
     is_gold_shop
     location
     city
     reputation
     clover
     is_official
    }
    badges {
      title
      image_url
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
""".trimIndent().replace("\n", " ")

internal fun getSimilarProductGQLQuery() = """
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