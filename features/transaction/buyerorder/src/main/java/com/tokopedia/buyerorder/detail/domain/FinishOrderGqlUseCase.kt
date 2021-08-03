package com.tokopedia.buyerorder.detail.domain

import com.tokopedia.buyerorder.detail.data.finishorder.BuyerFinishOrder
import com.tokopedia.buyerorder.detail.data.finishorder.BuyerFinishOrderParam
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by fwidjaja on 21/11/20.
 */
class FinishOrderGqlUseCase @Inject constructor(private val graphqlUseCase: GraphqlUseCase) : UseCase<BuyerFinishOrder.Data>() {
    companion object {
        const val REQUEST_PARAM_KEY_FINISH_ORDER_REQUEST = "REQUEST_PARAM_KEY_FINISH_ORDER_REQUEST"
        private const val PARAM_INPUT = "input"
    }

    private var query: String = ""
    private var params: MutableMap<String, Any> = mutableMapOf()

    fun setup(query: String, params: MutableMap<String, Any>) {
        this.query = query
        this.params = params
    }

    override fun createObservable(requestParams: RequestParams): Observable<BuyerFinishOrder.Data> {
        val graphqlRequest = GraphqlRequest(query, BuyerFinishOrder.Data::class.java, params, false)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(RequestParams.EMPTY).map {
            val uohFinishOrderData = it.getData<BuyerFinishOrder.Data>(BuyerFinishOrder.Data::class.java)
            uohFinishOrderData
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    private fun getParams(finishOrderParam: BuyerFinishOrderParam): Map<String, Any> {
        return mapOf(
                PARAM_INPUT to finishOrderParam)
    }
}