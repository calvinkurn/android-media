package com.tokopedia.purchase_platform.features.cart.domain.usecase

import android.content.Context

import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.features.cart.data.model.response.recentview.GqlRecentViewResponse
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase

import java.util.HashMap

import rx.Subscriber
import javax.inject.Inject

/**
 * Created by Irfan Khoirul on 21/09/18.
 */

class GetRecentViewUseCase @Inject constructor() {

    val graphqlUseCase = GraphqlUseCase()

    fun createObservable(userId: Int, graphqlResponseSubscriber: Subscriber<GraphqlResponse>) {
        graphqlUseCase.clearRequest()
        val variables = HashMap<String, Any>()
        variables[USER_ID] = userId

        val graphqlRequest = GraphqlRequest(QUERY, GqlRecentViewResponse::class.java, variables)

        graphqlUseCase.addRequest(graphqlRequest)
        graphqlUseCase.execute(graphqlResponseSubscriber)
    }

    fun unsubscribe() {
        graphqlUseCase.unsubscribe()
    }

    companion object {
        private val USER_ID = "userID"
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
