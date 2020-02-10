package com.tokopedia.purchase_platform.common.usecase

import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.purchase_platform.common.constant.CartConstant
import com.tokopedia.purchase_platform.common.data.model.response.updatecartcounter.UpdateCartCounterGqlResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by Irfan Khoirul on 2019-08-15.
 */

class UpdateCartCounterUseCase @Inject constructor(
        @Named(CartConstant.MUTATION_UPDATE_CART_COUNTER) private val queryString: String,
        private val graphqlUseCase: GraphqlUseCase) : UseCase<Int>() {

    override fun createObservable(p0: RequestParams?): Observable<Int> {
        val graphqlRequest = GraphqlRequest(queryString, UpdateCartCounterGqlResponse::class.java)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(RequestParams.EMPTY).map {
            val updateCartCounterResponse = it.getData<UpdateCartCounterGqlResponse>(UpdateCartCounterGqlResponse::class.java)
            updateCartCounterResponse.updateCartCounter.count
        }
    }

}
