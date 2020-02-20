package com.tokopedia.purchase_platform.features.cart.domain.usecase

import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.purchase_platform.common.domain.schedulers.ExecutorSchedulers
import com.tokopedia.purchase_platform.features.cart.data.model.response.recentview.GqlRecentViewResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

/**
 * Created by Irfan Khoirul on 21/09/18.
 */

class GetRecentViewUseCase @Inject constructor(val schedulers: ExecutorSchedulers) : UseCase<GqlRecentViewResponse>() {

    override fun createObservable(params: RequestParams): Observable<GqlRecentViewResponse> {
        val variables = HashMap<String, Any>()
        variables[USER_ID] = params.getInt(PARAM_USER_ID, 0)

        val graphqlRequest = GraphqlRequest(QUERY, GqlRecentViewResponse::class.java, variables)
        val graphqlUseCase = GraphqlUseCase()
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(RequestParams.EMPTY)
                .map {
                    it.getData<GqlRecentViewResponse>(GqlRecentViewResponse::class.java)
                }
                .subscribeOn(schedulers.io)
                .observeOn(schedulers.main)
    }

    companion object {
        private val USER_ID = "userID"
        val PARAM_USER_ID = "PARAM_USER_ID"
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
