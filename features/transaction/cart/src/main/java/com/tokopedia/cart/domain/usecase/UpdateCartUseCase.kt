package com.tokopedia.cart.domain.usecase

import com.tokopedia.cart.data.model.request.UpdateCartRequest
import com.tokopedia.cart.data.model.response.updatecart.UpdateCartGqlResponse
import com.tokopedia.cart.domain.mapper.mapUpdateCartData
import com.tokopedia.cart.domain.model.updatecart.UpdateCartData
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper.Companion.KEY_CHOSEN_ADDRESS
import com.tokopedia.purchase_platform.common.schedulers.ExecutorSchedulers
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

/**
 * Created by Irfan Khoirul on 2019-12-26.
 */

class UpdateCartUseCase @Inject constructor(private val graphqlUseCase: GraphqlUseCase,
                                            private val schedulers: ExecutorSchedulers,
                                            private val chosenAddressRequestHelper: ChosenAddressRequestHelper) : UseCase<UpdateCartData>() {

    companion object {
        val PARAM_UPDATE_CART_REQUEST = "PARAM_UPDATE_CART_REQUEST"
        val PARAM_CARTS = "carts"

        private const val PARAM_KEY_LANG = "lang"
        private const val PARAM_VALUE_ID = "id"
    }

    override fun createObservable(requestParams: RequestParams?): Observable<UpdateCartData> {
        val paramUpdateList = requestParams?.getObject(PARAM_UPDATE_CART_REQUEST) as ArrayList<UpdateCartRequest>

        val variables = mapOf(
                PARAM_KEY_LANG to PARAM_VALUE_ID,
                PARAM_CARTS to paramUpdateList,
                KEY_CHOSEN_ADDRESS to chosenAddressRequestHelper.getChosenAddress()
        )

        val mutation = getUpdateCartMutation()
        val graphqlRequest = GraphqlRequest(mutation, UpdateCartGqlResponse::class.java, variables)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)

        return graphqlUseCase.createObservable(RequestParams.EMPTY)
                .map {
                    val updateCartGqlResponse = it.getData<UpdateCartGqlResponse>(UpdateCartGqlResponse::class.java)
                    var updateCartData = UpdateCartData()
                    updateCartGqlResponse?.updateCartDataResponse?.data?.let {
                        updateCartData = mapUpdateCartData(updateCartGqlResponse, it)
                    }
                    updateCartData
                }
                .subscribeOn(schedulers.io)
                .observeOn(schedulers.main)
    }

}