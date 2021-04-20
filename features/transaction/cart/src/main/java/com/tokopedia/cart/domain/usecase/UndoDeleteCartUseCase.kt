package com.tokopedia.cart.domain.usecase

import com.tokopedia.atc_common.domain.usecase.UpdateCartCounterUseCase
import com.tokopedia.cart.data.model.request.UndoDeleteCartRequest
import com.tokopedia.cart.data.model.response.undodeletecart.UndoDeleteCartGqlResponse
import com.tokopedia.cart.domain.model.cartlist.UndoDeleteCartData
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.purchase_platform.common.schedulers.ExecutorSchedulers
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

class UndoDeleteCartUseCase @Inject constructor(private val graphqlUseCase: GraphqlUseCase,
                                                private val schedulers: ExecutorSchedulers) : UseCase<UndoDeleteCartData>() {

    companion object {
        const val PARAM_UNDO_REMOVE_CART_REQUEST = "PARAM_UNDO_REMOVE_CART_REQUEST"

        private const val PARAM_KEY_LANG = "lang"
        private const val PARAM_VALUE_ID = "id"
        private const val PARAM_KEY_CART_IDS = "cartIds"
        private const val STATUS_OK = "OK"
    }

    override fun createObservable(requestParams: RequestParams?): Observable<UndoDeleteCartData> {
        val paramDelete = requestParams?.getObject(PARAM_UNDO_REMOVE_CART_REQUEST) as UndoDeleteCartRequest

        val variables = mapOf(
                PARAM_KEY_LANG to PARAM_VALUE_ID,
                PARAM_KEY_CART_IDS to paramDelete.cartIds
        )

        val mutation = getUndoDeleteCartMutation()
        val graphqlRequest = GraphqlRequest(mutation, UndoDeleteCartGqlResponse::class.java, variables)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)

        return graphqlUseCase.createObservable(RequestParams.EMPTY)
                .map {
                    val undoDeleteCartGqlResponse = it.getData<UndoDeleteCartGqlResponse>(UndoDeleteCartGqlResponse::class.java)
                    val undoDeleteCartData = UndoDeleteCartData()
                    if (undoDeleteCartGqlResponse != null) {
                        undoDeleteCartData.isSuccess = undoDeleteCartGqlResponse.undoDeleteCartDataResponse.status == STATUS_OK
                        undoDeleteCartData.message = if (undoDeleteCartGqlResponse.undoDeleteCartDataResponse.status == STATUS_OK) {
                            if (undoDeleteCartGqlResponse.undoDeleteCartDataResponse.data.message.isNotEmpty()) {
                                undoDeleteCartGqlResponse.undoDeleteCartDataResponse.data.message[0]
                            } else {
                                ""
                            }
                        } else {
                            if (undoDeleteCartGqlResponse.undoDeleteCartDataResponse.errorMessage.isNotEmpty()) {
                                undoDeleteCartGqlResponse.undoDeleteCartDataResponse.errorMessage[0]
                            } else {
                                ""
                            }
                        }
                    }
                    undoDeleteCartData
                }
                .subscribeOn(schedulers.io)
                .observeOn(schedulers.main)
    }

}