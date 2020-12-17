package com.tokopedia.cart.domain.usecase

import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.purchase_platform.common.schedulers.ExecutorSchedulers
import com.tokopedia.cart.data.model.response.recentview.GqlRecentViewResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import java.util.*
import javax.inject.Inject

/**
 * Created by Irfan Khoirul on 21/09/18.
 */

class GetRecentViewUseCase @Inject constructor(val schedulers: ExecutorSchedulers) : UseCase<GqlRecentViewResponse>() {

    override fun createObservable(params: RequestParams): Observable<GqlRecentViewResponse> {
        val variables = HashMap<String, Any>()
        variables[USER_ID] = params.getInt(PARAM_USER_ID, 0)
        variables[FILTER] = mapOf(
                SOURCE to CART,
                BLACKLISTPRODUCTIDS to params.getString(PARAM_PRODUCT_IDS, "")
        )

        val query = getRecentViewQuery()
        val graphqlRequest = GraphqlRequest(query, GqlRecentViewResponse::class.java, variables)
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
        private const val FILTER = "filter"
        private const val BLACKLISTPRODUCTIDS = "blacklistProductIds"
        private const val SOURCE = "source"
        private const val CART = "cart"

        val PARAM_USER_ID = "PARAM_USER_ID"
        val PARAM_PRODUCT_IDS = "PARAM_PRODUCT_IDS"
    }
}
