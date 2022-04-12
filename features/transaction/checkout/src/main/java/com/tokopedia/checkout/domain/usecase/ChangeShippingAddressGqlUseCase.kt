package com.tokopedia.checkout.domain.usecase

import com.tokopedia.checkout.data.model.response.changeshippingaddress.ChangeShippingAddressGqlResponse
import com.tokopedia.checkout.domain.model.changeaddress.SetShippingAddressData
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.purchase_platform.common.constant.CartConstant.CART_ERROR_GLOBAL
import com.tokopedia.purchase_platform.common.exception.CartResponseErrorException
import com.tokopedia.purchase_platform.common.schedulers.ExecutorSchedulers
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject
import javax.inject.Named

class ChangeShippingAddressGqlUseCase @Inject constructor(@Named(CHANGE_SHIPPING_ADDRESS_MUTATION) private val queryString: String,
                                                          private val graphqlUseCase: GraphqlUseCase,
                                                          private val schedulers: ExecutorSchedulers) : UseCase<SetShippingAddressData>() {

    companion object {
        const val CHANGE_SHIPPING_ADDRESS_MUTATION = "CHANGE_SHIPPING_ADDRESS_MUTATION"

        const val CHANGE_SHIPPING_ADDRESS_PARAMS = "CHANGE_SHIPPING_ADDRESS_PARAMS"
        const val PARAM_CARTS = "carts"
        const val PARAM_ONE_CLICK_SHIPMENT = "is_one_click_shipment"
    }

    override fun createObservable(requestParam: RequestParams): Observable<SetShippingAddressData> {
        val params = requestParam.parameters[CHANGE_SHIPPING_ADDRESS_PARAMS] as HashMap<String, Any>
        val graphqlRequest = GraphqlRequest(queryString, ChangeShippingAddressGqlResponse::class.java, params)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(RequestParams.EMPTY)
                .map {
                    val gqlResponse = it.getData<ChangeShippingAddressGqlResponse>(ChangeShippingAddressGqlResponse::class.java)
                    if (gqlResponse != null) {
                        SetShippingAddressData().apply {
                            isSuccess = gqlResponse.changeShippingAddressResponse.dataResponse.success == 1
                            messages = gqlResponse.changeShippingAddressResponse.dataResponse.messages ?: emptyList()
                        }
                    } else {
                        throw CartResponseErrorException(CART_ERROR_GLOBAL)
                    }
                }
                .subscribeOn(schedulers.io)
                .observeOn(schedulers.main)
    }

}