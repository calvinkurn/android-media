package com.tokopedia.checkout.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.checkout.data.model.response.shipmentaddressform.ShipmentAddressFormGqlResponse
import com.tokopedia.checkout.domain.mapper.ShipmentMapper
import com.tokopedia.checkout.domain.model.cartshipmentform.CartShipmentAddressFormData
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper
import com.tokopedia.purchase_platform.common.constant.CartConstant
import com.tokopedia.purchase_platform.common.exception.CartResponseErrorException
import com.tokopedia.usecase.coroutines.UseCase
import timber.log.Timber
import javax.inject.Inject

class GetShipmentAddressFormV3UseCase @Inject constructor(
    @ApplicationContext private val graphqlRepository: GraphqlRepository,
    private val shipmentMapper: ShipmentMapper,
    private val chosenAddressRequestHelper: ChosenAddressRequestHelper
) : UseCase<CartShipmentAddressFormData>() {

    private var params: Map<String, Any?>? = null

    fun setParams(
        isOneClickShipment: Boolean,
        isTradeIn: Boolean,
        isSkipUpdateOnboardingState: Boolean,
        cornerId: String?,
        deviceId: String?,
        leasingId: String?,
        isPlusSelected: Boolean
    ) {
        val params: MutableMap<String, Any?> = HashMap()
        params[ChosenAddressRequestHelper.KEY_CHOSEN_ADDRESS] =
            chosenAddressRequestHelper.getChosenAddress()
        params[PARAM_KEY_LANG] = "id"
        params[PARAM_KEY_IS_ONE_CLICK_SHIPMENT] = isOneClickShipment
        params[PARAM_KEY_SKIP_ONBOARDING_UPDATE_STATE] = if (isSkipUpdateOnboardingState) 1 else 0
        if (cornerId != null) {
            try {
                val tmpCornerId = cornerId.toIntOrZero()
                params[PARAM_KEY_CORNER_ID] = tmpCornerId
            } catch (e: NumberFormatException) {
                Timber.d(e)
            }
        }
        if (leasingId != null && !leasingId.isEmpty()) {
            try {
                val tmpLeasingId = leasingId.toIntOrZero()
                params[PARAM_KEY_VEHICLE_LEASING_ID] = tmpLeasingId
            } catch (e: NumberFormatException) {
                Timber.d(e)
            }
        }
        if (isTradeIn) {
            params[PARAM_KEY_IS_TRADEIN] = true
            params[PARAM_KEY_DEVICE_ID] = deviceId ?: ""
        }
        params[PARAM_KEY_IS_PLUS_SELECTED] = isPlusSelected

        this.params = mapOf(
            "params" to params
        )
    }

    @GqlQuery(QUERY_SHIPMENT_ADDRESS_FORM, SHIPMENT_ADDRESS_FORM_V3_QUERY)
    override suspend fun executeOnBackground(): CartShipmentAddressFormData {
        if (params == null) {
            throw RuntimeException("Parameter is null!")
        }

        val request = GraphqlRequest(
            ShipmentAddressFormQuery(),
            ShipmentAddressFormGqlResponse::class.java,
            params
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

        private const val QUERY_SHIPMENT_ADDRESS_FORM = "ShipmentAddressFormQuery"
    }

}
