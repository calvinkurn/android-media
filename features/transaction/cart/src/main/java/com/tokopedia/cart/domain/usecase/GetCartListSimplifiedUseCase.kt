package com.tokopedia.cart.domain.usecase

import com.tokopedia.cart.data.model.response.shopgroupsimplified.ShopGroupSimplifiedGqlResponse
import com.tokopedia.cart.domain.mapper.CartSimplifiedMapper
import com.tokopedia.cart.domain.model.cartlist.CartListData
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper.Companion.KEY_CHOSEN_ADDRESS
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.purchase_platform.common.exception.CartResponseErrorException
import com.tokopedia.purchase_platform.common.schedulers.ExecutorSchedulers
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

/**
 * Created by Irfan Khoirul on 2019-10-18.
 */

class GetCartListSimplifiedUseCase @Inject constructor(private val graphqlUseCase: GraphqlUseCase,
                                                       private val cartSimplifiedMapper: CartSimplifiedMapper,
                                                       private val schedulers: ExecutorSchedulers,
                                                       private val chosenAddressRequestHelper: ChosenAddressRequestHelper) : UseCase<CartListData>() {

    companion object {
        const val PARAM_GET_CART = "PARAM_GET_CART"

        const val PARAM_KEY_LANG = "lang"
        const val PARAM_KEY_SELECTED_CART_ID = "selected_cart_id"
        const val PARAM_KEY_ADDITIONAL = "additional_params"

        const val PARAM_VALUE_ID = "id"
    }

    fun buildParams(cartId: String): Map<String, Any?> {
        return mapOf(
                PARAM_KEY_LANG to PARAM_VALUE_ID,
                PARAM_KEY_SELECTED_CART_ID to cartId,
                PARAM_KEY_ADDITIONAL to mapOf(
                        KEY_CHOSEN_ADDRESS to chosenAddressRequestHelper.getChosenAddress()
                )
        )
    }

    override fun createObservable(requestParam: RequestParams?): Observable<CartListData> {
        val params = requestParam?.getObject(PARAM_GET_CART) as Map<String, Any?>
        val queryString = getQueryCartRevamp()
        val graphqlRequest = GraphqlRequest(queryString, ShopGroupSimplifiedGqlResponse::class.java, params)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(RequestParams.EMPTY)
                .map {
                    val shopGroupSimplifiedGqlResponse = it.getData<ShopGroupSimplifiedGqlResponse>(ShopGroupSimplifiedGqlResponse::class.java)
                    if (shopGroupSimplifiedGqlResponse != null) {
                        if (shopGroupSimplifiedGqlResponse.shopGroupSimplifiedResponse.status == "OK") {
                            cartSimplifiedMapper.convertToCartItemDataList(shopGroupSimplifiedGqlResponse.shopGroupSimplifiedResponse.data)
                        } else {
                            if (shopGroupSimplifiedGqlResponse.shopGroupSimplifiedResponse.errorMessages.isNotEmpty()) {
                                throw CartResponseErrorException(shopGroupSimplifiedGqlResponse.shopGroupSimplifiedResponse.errorMessages.joinToString())
                            } else {
                                throw ResponseErrorException()
                            }
                        }
                    } else {
                        throw ResponseErrorException()
                    }
                }
                .subscribeOn(schedulers.io)
                .observeOn(schedulers.main)
    }

}