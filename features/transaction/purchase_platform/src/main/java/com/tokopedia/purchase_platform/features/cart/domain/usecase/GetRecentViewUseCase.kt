package com.tokopedia.purchase_platform.features.cart.domain.usecase

import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.purchase_platform.features.cart.data.model.response.recentview.GqlRecentViewResponse
import rx.Subscriber
import java.util.*
import javax.inject.Inject

/**
 * Created by Irfan Khoirul on 21/09/18.
 */

class GetRecentViewUseCase @Inject constructor() {

    val graphqlUseCase = GraphqlUseCase()

    fun createObservable(userId: Int, allProductIds: List<String>, graphqlResponseSubscriber: Subscriber<GraphqlResponse>) {
        graphqlUseCase.clearRequest()
        val variables = HashMap<String, Any>()
        variables[USER_ID] = userId
        variables[FILTER] = mapOf(
                SOURCE to CART,
                BLACKLISTPRODUCTIDS to allProductIds.joinToString()
        )

        val graphqlRequest = GraphqlRequest(QUERY, GqlRecentViewResponse::class.java, variables)

        graphqlUseCase.addRequest(graphqlRequest)
        graphqlUseCase.execute(graphqlResponseSubscriber)
    }

    fun unsubscribe() {
        graphqlUseCase.unsubscribe()
    }

    companion object {
        private val USER_ID = "userID"
        private val FILTER = "filter"
        private val BLACKLISTPRODUCTIDS = "blacklistProductIds"
        private val SOURCE = "source"
        private val CART = "cart"
    }

    val QUERY = """
        query recent_view(${'$'}userID: Int){
          get_recent_view(userID: ${'$'}userID){
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
    """.trimIndent()

}
