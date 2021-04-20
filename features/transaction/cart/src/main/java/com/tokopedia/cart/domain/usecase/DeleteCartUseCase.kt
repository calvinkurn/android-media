package com.tokopedia.cart.domain.usecase

import com.tokopedia.atc_common.domain.usecase.UpdateCartCounterUseCase
import com.tokopedia.cart.data.model.request.RemoveCartRequest
import com.tokopedia.cart.data.model.response.deletecart.DeleteCartGqlResponse
import com.tokopedia.cart.domain.model.cartlist.DeleteCartData
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.purchase_platform.common.schedulers.ExecutorSchedulers
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

/**
 * Created by Irfan Khoirul on 2019-12-26.
 */

class DeleteCartUseCase @Inject constructor(private val graphqlUseCase: GraphqlUseCase,
                                            private val schedulers: ExecutorSchedulers) : UseCase<DeleteCartData>() {

    companion object {
        const val PARAM_REMOVE_CART_REQUEST = "PARAM_REMOVE_CART_REQUEST"

        private const val PARAM_KEY_LANG = "lang"
        private const val PARAM_VALUE_ID = "id"
        private const val PARAM_KEY_ADD_TO_WISHLIST = "addWishlist"
        private const val PARAM_KEY_CART_IDS = "cartIds"
    }

    override fun createObservable(requestParams: RequestParams?): Observable<DeleteCartData> {
        val paramDelete = requestParams?.getObject(PARAM_REMOVE_CART_REQUEST) as RemoveCartRequest

        val variables = mapOf(
                PARAM_KEY_LANG to PARAM_VALUE_ID,
                PARAM_KEY_ADD_TO_WISHLIST to paramDelete.addWishlist,
                PARAM_KEY_CART_IDS to paramDelete.cartIds
        )

        val mutation = getDeleteCartMutation()
        val graphqlRequest = GraphqlRequest(mutation, DeleteCartGqlResponse::class.java, variables)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)

        return graphqlUseCase.createObservable(RequestParams.EMPTY)
                .map {
                    val deleteCartGqlResponse = it.getData<DeleteCartGqlResponse>(DeleteCartGqlResponse::class.java)
                    val deleteCartData = DeleteCartData()
                    if (deleteCartGqlResponse != null) {
                        deleteCartData.isSuccess = deleteCartGqlResponse.deleteCartDataResponse.status == "OK"
                        deleteCartData.message = if (deleteCartGqlResponse.deleteCartDataResponse.status == "OK") {
                            if (deleteCartGqlResponse.deleteCartDataResponse.data?.message?.isNotEmpty() == true) {
                                deleteCartGqlResponse.deleteCartDataResponse.data.message[0]
                            } else {
                                ""
                            }
                        } else {
                            if (deleteCartGqlResponse.deleteCartDataResponse.errorMessage.isNotEmpty()) {
                                deleteCartGqlResponse.deleteCartDataResponse.errorMessage[0]
                            } else {
                                ""
                            }
                        }
                    }
                    deleteCartData
                }
                .subscribeOn(schedulers.io)
                .observeOn(schedulers.main)
    }

}