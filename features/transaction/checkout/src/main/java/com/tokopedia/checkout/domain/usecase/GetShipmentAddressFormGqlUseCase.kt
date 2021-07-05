package com.tokopedia.checkout.domain.usecase

import com.tokopedia.checkout.data.model.response.shipmentaddressform.ShipmentAddressFormGqlResponse
import com.tokopedia.checkout.domain.mapper.ShipmentMapper
import com.tokopedia.checkout.domain.model.cartshipmentform.CartShipmentAddressFormData
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.purchase_platform.common.constant.CartConstant.CART_ERROR_GLOBAL
import com.tokopedia.purchase_platform.common.exception.CartResponseErrorException
import com.tokopedia.purchase_platform.common.feature.localizationchooseaddress.request.ChosenAddressRequestHelper
import com.tokopedia.purchase_platform.common.schedulers.ExecutorSchedulers
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject
import javax.inject.Named

class GetShipmentAddressFormGqlUseCase @Inject constructor(@Named(SHIPMENT_ADDRESS_FORM_QUERY) private val queryString: String,
                                                           private val graphqlUseCase: GraphqlUseCase,
                                                           private val shipmentMapper: ShipmentMapper,
                                                           private val schedulers: ExecutorSchedulers,
                                                           private val chosenAddressRequestHelper: ChosenAddressRequestHelper) : UseCase<CartShipmentAddressFormData>() {

    companion object {
        const val SHIPMENT_ADDRESS_FORM_QUERY = "SHIPMENT_ADDRESS_FORM_QUERY"
        const val SHIPMENT_ADDRESS_FORM_PARAMS = "params"

        const val PARAM_KEY_LANG = "lang"
        const val PARAM_KEY_IS_ONE_CLICK_SHIPMENT = "is_ocs"
        const val PARAM_KEY_CORNER_ID = "corner_id"
        const val PARAM_KEY_SKIP_ONBOARDING_UPDATE_STATE = "skip_onboarding"
        const val PARAM_KEY_IS_TRADEIN = "is_trade_in"
        const val PARAM_KEY_DEVICE_ID = "dev_id"
        const val PARAM_KEY_VEHICLE_LEASING_ID = "vehicle_leasing_id"
    }

    override fun createObservable(requestParam: RequestParams): Observable<CartShipmentAddressFormData> {
        chosenAddressRequestHelper.addChosenAddressParam(requestParam)
        val params = mapOf(SHIPMENT_ADDRESS_FORM_PARAMS to requestParam.parameters)
        val graphqlRequest = GraphqlRequest(queryString, ShipmentAddressFormGqlResponse::class.java, params)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(RequestParams.EMPTY)
                .map {
                    val gqlResponse = it.getData<ShipmentAddressFormGqlResponse>(ShipmentAddressFormGqlResponse::class.java)
                    if (gqlResponse != null) {
                        if (gqlResponse.shipmentAddressFormResponse.status == "OK") {
                            shipmentMapper.convertToShipmentAddressFormData(gqlResponse.shipmentAddressFormResponse.data)
                        } else {
                            if (gqlResponse.shipmentAddressFormResponse.errorMessages.isNotEmpty()) {
                                throw CartResponseErrorException(gqlResponse.shipmentAddressFormResponse.errorMessages.joinToString())
                            } else {
                                throw CartResponseErrorException(CART_ERROR_GLOBAL)
                            }
                        }
                    } else {
                        throw CartResponseErrorException(CART_ERROR_GLOBAL)
                    }
                }
                .subscribeOn(schedulers.io)
                .observeOn(schedulers.main)
    }
}