package com.tokopedia.cart.domain.usecase

fun getRecentViewQuery(): String {
    return """
        query recent_view(${'$'}userID: Int, ${'$'}filter: Filter){
          get_recent_view(userID: ${'$'}userID, filter: ${'$'}filter){
            items{
              product_id,
              product_name,
              product_price,
              shop_url,
              product_rating,
              product_review_count,
              product_image,
              shop_id,
              shop_name,
              shop_location,
              badges {
                title
                image_url
              }
              wishlist
            }
          }
        }

    """
}