package com.tokopedia.recentview.data.query

/**
 * @author by yoasfs on 12/08/20
 */
object RecentViewQuery{
    fun getQuery() = """
         query getRecentView(${'$'}userID: String!, ${'$'}count:Int) {
                get_recent_view(userID: ${'$'}userID, count:${'$'}count,filter:{
                     blacklistProductIds:"1,2,3",
                     source:"any",
                   }) {
                     items {
                       product_id
                       product_name
                       product_url
                       product_image
                       product_price
                       product_rating
                       product_review_count
                       shop_id
                       shop_url
                       shop_location
                       shop_name
                       shop_gold_status
                       wishlist
                       badges{
                         title
                         image_url
                       }
                       labels {
                         title
                         color
                       }
                     }
                   }
            }
    """.trimIndent()
}