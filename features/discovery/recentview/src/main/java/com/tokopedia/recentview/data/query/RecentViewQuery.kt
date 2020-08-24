package com.tokopedia.recentview.data.query

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.recentview.data.entity.Badge
import com.tokopedia.recentview.data.entity.Label
import java.util.*

/**
 * @author by yoasfs on 12/08/20
 */
object RecentViewQuery{
    fun getQuery() = """
         query getRecentView(${'$'}userID: Int!) {
                get_recent_view(userID: ${'$'}userID) {
                     items {
                       product_id
                       product_name
                       product_url
                       product_image
                       product_price
                       shop_id
                       shop_url
                       shop_location
                       shop_name
                       shop_gold_status
                       rating
                       review_count
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