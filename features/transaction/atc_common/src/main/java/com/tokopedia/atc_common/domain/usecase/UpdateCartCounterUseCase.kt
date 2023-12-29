package com.tokopedia.atc_common.domain.usecase

import com.tokopedia.atc_common.AtcConstant
import com.tokopedia.atc_common.domain.model.response.updatecartcounter.UpdateCartCounterGqlResponse
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by Irfan Khoirul on 2019-08-15.
 */

class UpdateCartCounterUseCase @Inject constructor(
    @Named(AtcConstant.MUTATION_UPDATE_CART_COUNTER) private val queryString: String
) : UseCase<Int>() {

    override fun createObservable(p0: RequestParams?): Observable<Int> {
        val graphqlRequest = GraphqlRequest(queryString, UpdateCartCounterGqlResponse::class.java)
        val graphqlUseCase = GraphqlUseCase()
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(RequestParams.EMPTY).map {
            val updateCartCounterResponse = it.getData<UpdateCartCounterGqlResponse>(UpdateCartCounterGqlResponse::class.java)
            updateCartCounterResponse.updateCartCounter.count
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}
