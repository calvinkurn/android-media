package com.tokopedia.checkout.domain.usecase

import com.tokopedia.checkout.data.model.response.saveshipmentstate.SaveShipmentStateGqlResponse
import com.tokopedia.checkout.domain.model.saveshipmentstate.SaveShipmentStateData
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.purchase_platform.common.constant.CartConstant.CART_ERROR_GLOBAL
import com.tokopedia.purchase_platform.common.exception.CartResponseErrorException
import com.tokopedia.purchase_platform.common.schedulers.ExecutorSchedulers
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

class SaveShipmentStateGqlUseCase @Inject constructor(
    private val graphqlUseCase: GraphqlUseCase,
    private val schedulers: ExecutorSchedulers
) : UseCase<SaveShipmentStateData>() {

    companion object {
        const val PARAM_CART_DATA_OBJECT = "PARAM_CART_DATA_OBJECT"
        const val PARAM_CARTS = "carts"
    }

    override fun createObservable(requestParam: RequestParams): Observable<SaveShipmentStateData> {
        val params = requestParam.parameters[PARAM_CART_DATA_OBJECT] as HashMap<String, Any>
        val graphqlRequest = GraphqlRequest(SAVE_SHIPMENT_MUTATION, SaveShipmentStateGqlResponse::class.java, params)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(RequestParams.EMPTY)
            .map {
                val gqlResponse = it.getData<SaveShipmentStateGqlResponse>(SaveShipmentStateGqlResponse::class.java)
                if (gqlResponse != null) {
                    SaveShipmentStateData().apply {
                        isSuccess = gqlResponse.saveShipmentStateResponse.success == 1
                        error = gqlResponse.saveShipmentStateResponse.error
                        message = gqlResponse.saveShipmentStateResponse.message
                    }
                } else {
                    throw CartResponseErrorException(CART_ERROR_GLOBAL)
                }
            }
            .subscribeOn(schedulers.io)
            .observeOn(schedulers.main)
    }
}

const val SAVE_SHIPMENT_MUTATION = """
    mutation save_shipment(${"$"}carts: [SaveShipmentParam]) {
        save_shipment(carts: ${"$"}carts) {
            error_message
            status
            data {
                success
                error
                message
            }
        }
    }
"""
