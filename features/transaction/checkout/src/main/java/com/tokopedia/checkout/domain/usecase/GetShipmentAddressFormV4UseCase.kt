package com.tokopedia.checkout.domain.usecase

import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.checkout.data.model.request.saf.ShipmentAddressFormRequest
import com.tokopedia.checkout.data.model.response.shipmentaddressform.ShipmentAddressFormGqlResponse
import com.tokopedia.checkout.domain.mapper.ShipmentMapper
import com.tokopedia.checkout.domain.model.cartshipmentform.CartShipmentAddressFormData
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper
import com.tokopedia.purchase_platform.common.constant.CartConstant
import com.tokopedia.purchase_platform.common.exception.CartResponseErrorException
import timber.log.Timber
import javax.inject.Inject

class GetShipmentAddressFormV4UseCase @Inject constructor(
    @ApplicationContext private val graphqlRepository: GraphqlRepository,
    private val shipmentMapper: ShipmentMapper,
    private val chosenAddressRequestHelper: ChosenAddressRequestHelper,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<ShipmentAddressFormRequest, CartShipmentAddressFormData>(dispatchers.io) {

    @GqlQuery(QUERY_SHIPMENT_ADDRESS_FORM, SHIPMENT_ADDRESS_FORM_V4_QUERY)
    override fun graphqlQuery(): String {
        return SHIPMENT_ADDRESS_FORM_V4_QUERY
    }

    override suspend fun execute(params: ShipmentAddressFormRequest): CartShipmentAddressFormData {
        val gson = Gson()
        val paramMap: MutableMap<String, Any> = HashMap()
        paramMap[ChosenAddressRequestHelper.KEY_CHOSEN_ADDRESS] =
            chosenAddressRequestHelper.getChosenAddress()
        paramMap[PARAM_KEY_LANG] = "id"
        paramMap[PARAM_KEY_IS_ONE_CLICK_SHIPMENT] = params.isOneClickShipment
        paramMap[PARAM_KEY_SKIP_ONBOARDING_UPDATE_STATE] = if (params.isSkipUpdateOnboardingState) 1 else 0
        if (params.cornerId != null) {
            try {
                val tmpCornerId = params.cornerId.toLongOrZero()
                paramMap[PARAM_KEY_CORNER_ID] = tmpCornerId
            } catch (e: NumberFormatException) {
                Timber.d(e)
            }
        }
        if (!params.leasingId.isNullOrEmpty()) {
            try {
                val tmpLeasingId = params.leasingId.toLongOrZero()
                paramMap[PARAM_KEY_VEHICLE_LEASING_ID] = tmpLeasingId
            } catch (e: NumberFormatException) {
                Timber.d(e)
            }
        }
        if (params.isTradeIn) {
            paramMap[PARAM_KEY_IS_TRADEIN] = true
            paramMap[PARAM_KEY_DEVICE_ID] = params.deviceId ?: ""
        }
        paramMap[PARAM_KEY_IS_PLUS_SELECTED] = params.isPlusSelected
        paramMap[PARAM_KEY_IS_CHECKOUT_REIMAGINE] = params.isCheckoutReimagine

        val request = GraphqlRequest(
            ShipmentAddressFormQuery(),
            ShipmentAddressFormGqlResponse::class.java,
            mapOf(
                "params" to paramMap
            )
        )
        val response = graphqlRepository.response(listOf(request))
            .getSuccessData<ShipmentAddressFormGqlResponse>()

        if (response.shipmentAddressFormResponse.status == "OK") {
            return shipmentMapper.convertToShipmentAddressFormData(response.shipmentAddressFormResponse.data)
        } else {
            if (response.shipmentAddressFormResponse.errorMessages.isNotEmpty()) {
                throw CartResponseErrorException(response.shipmentAddressFormResponse.errorMessages.joinToString())
            } else {
                throw CartResponseErrorException(CartConstant.CART_ERROR_GLOBAL)
            }
        }
    }

    companion object {
        private const val PARAM_KEY_LANG = "lang"
        private const val PARAM_KEY_IS_ONE_CLICK_SHIPMENT = "is_ocs"
        private const val PARAM_KEY_CORNER_ID = "corner_id"
        private const val PARAM_KEY_SKIP_ONBOARDING_UPDATE_STATE = "skip_onboarding"
        private const val PARAM_KEY_IS_TRADEIN = "is_trade_in"
        private const val PARAM_KEY_DEVICE_ID = "dev_id"
        private const val PARAM_KEY_VEHICLE_LEASING_ID = "vehicle_leasing_id"
        private const val PARAM_KEY_IS_PLUS_SELECTED = "is_plus_selected"
        private const val PARAM_KEY_IS_CHECKOUT_REIMAGINE = "is_checkout_reimagine"

        private const val QUERY_SHIPMENT_ADDRESS_FORM = "ShipmentAddressFormQuery"
    }
}
