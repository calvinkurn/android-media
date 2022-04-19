package com.tokopedia.topads.dashboard.data.raw

/**
 * Created by Pika on 11/12/20.
 */
const val PRODUCT_RECOMMENDATION = """
    query topadsGetProductRecommendationV2(${'$'}shop_id : String!){
  topadsGetProductRecommendationV2(shop_id:${'$'}shop_id){
    data {
      info
      nominal_id
      products {
          product_id
          product_name
          image_url
          search_count
          search_percent
          recommended_bid
          min_bid
      }
    }
    errors{
      code
      detail
    }
  }
}
"""
