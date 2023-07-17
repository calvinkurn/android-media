package com.tokopedia.checkout.revamp.view.processor

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.checkout.domain.model.cartshipmentform.CartShipmentAddressFormData
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutOrderShipment
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutPageState
import com.tokopedia.logisticCommon.data.constant.AddressConstant
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticCommon.data.response.KeroAddrIsEligibleForAddressFeatureData
import com.tokopedia.logisticCommon.domain.param.EditAddressParam
import com.tokopedia.logisticCommon.domain.usecase.EditAddressUseCase
import com.tokopedia.logisticCommon.domain.usecase.EligibleForAddressUseCase
import com.tokopedia.logisticcart.scheduledelivery.domain.usecase.GetRatesWithScheduleDeliveryCoroutineUseCase
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.RatesResponseStateConverter
import com.tokopedia.logisticcart.shipping.model.RatesParam
import com.tokopedia.logisticcart.shipping.model.ShopShipment
import com.tokopedia.logisticcart.shipping.usecase.GetRatesApiCoroutineUseCase
import com.tokopedia.logisticcart.shipping.usecase.GetRatesCoroutineUseCase
import com.tokopedia.network.authentication.AuthHelper
import com.tokopedia.network.utils.TKPDMapParam
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class CheckoutLogisticProcessor @Inject constructor(
    private val eligibleForAddressUseCase: Lazy<EligibleForAddressUseCase>,
    private val editAddressUseCase: EditAddressUseCase,
    private val ratesUseCase: GetRatesCoroutineUseCase,
    private val ratesApiUseCase: GetRatesApiCoroutineUseCase,
    private val ratesWithScheduleUseCase: GetRatesWithScheduleDeliveryCoroutineUseCase,
    private val ratesResponseStateConverter: RatesResponseStateConverter,
    private val userSessionInterface: UserSessionInterface,
    private val dispatchers: CoroutineDispatchers
) {

    fun checkIsUserEligibleForRevampAna(
        cartShipmentAddressFormData: CartShipmentAddressFormData,
        callback: (CheckoutPageState) -> Unit
    ) {
        eligibleForAddressUseCase.get()
            .eligibleForAddressFeature({ response: KeroAddrIsEligibleForAddressFeatureData ->
                callback(
                    CheckoutPageState.NoAddress(
                        cartShipmentAddressFormData,
                        response.eligibleForRevampAna.eligible
                    )
                )
            }, { throwable: Throwable ->
//                    var errorMessage = throwable.message
//            if (errorMessage == null) {
//                errorMessage =
//                    view?.getStringResource(com.tokopedia.abstraction.R.string.default_request_error_unknown_short)
//            }
//            view?.showToastError(errorMessage)
                callback(CheckoutPageState.Error(throwable))
            }, AddressConstant.ANA_REVAMP_FEATURE_ID)
    }

    suspend fun editAddressPinpoint(
        latitude: String,
        longitude: String,
        recipientAddressModel: RecipientAddressModel
    ): EditAddressResult {
        return withContext(dispatchers.io) {
            return@withContext try {
                val requestParams =
                    generateEditAddressRequestParams(latitude, longitude, recipientAddressModel)
                val stringResponse =
                    editAddressUseCase.createObservable(requestParams).toBlocking().single()
//            if (view != null) {
//                view!!.setHasRunningApiCall(false)
//                view!!.hideLoading()
                var response: JsonObject? = null
                var messageError = ""
                var statusSuccess: Boolean
                try {
                    response = JsonParser().parse(stringResponse).asJsonObject
                    val statusCode =
                        response.asJsonObject.getAsJsonObject(EditAddressUseCase.RESPONSE_DATA)[EditAddressUseCase.RESPONSE_IS_SUCCESS].asInt
                    statusSuccess = statusCode == 1
                    if (!statusSuccess) {
                        messageError =
                            response.getAsJsonArray("message_error")[0].asString
                    }
                } catch (e: Exception) {
                    Timber.d(e)
                    statusSuccess = false
                }
                if (response != null && statusSuccess) {
                    recipientAddressModel.latitude = latitude
                    recipientAddressModel.longitude = longitude
                    EditAddressResult(isSuccess = true)
//                    view?.renderEditAddressSuccess(latitude, longitude)
                } else {
//                    if (messageError.isEmpty()) {
//                        messageError =
//                            view?.getStringResource(com.tokopedia.abstraction.R.string.default_request_error_unknown)
//                                ?: ""
//                    }
//                    view?.navigateToSetPinpoint(messageError, locationPass)
                    EditAddressResult(isSuccess = false, errorMessage = messageError)
                }
//            }
            } catch (t: Throwable) {
                val exception = getActualThrowableForRx(t)
                Timber.d(exception)
                EditAddressResult(isSuccess = false, throwable = t)
//            if (view != null) {
//                view!!.setHasRunningApiCall(false)
//                view!!.hideLoading()
//                view!!.showToastError(
//                    ErrorHandler.getErrorMessage(
//                        view!!.activity,
//                        exception
//                    )
//                )
//            }
            }
        }
    }

    private fun generateEditAddressRequestParams(
        addressLatitude: String,
        addressLongitude: String,
        recipientAddressModel: RecipientAddressModel
    ): RequestParams {
        val params: MutableMap<String, String> = AuthHelper.generateParamsNetwork(
            userSessionInterface.userId,
            userSessionInterface.deviceId,
            TKPDMapParam()
        )
        params[EditAddressParam.ADDRESS_ID] = recipientAddressModel.id
        params[EditAddressParam.ADDRESS_NAME] = recipientAddressModel.addressName
        params[EditAddressParam.ADDRESS_STREET] =
            recipientAddressModel.street
        params[EditAddressParam.POSTAL_CODE] = recipientAddressModel.postalCode
        params[EditAddressParam.DISTRICT_ID] = recipientAddressModel.destinationDistrictId
        params[EditAddressParam.CITY_ID] = recipientAddressModel.cityId
        params[EditAddressParam.PROVINCE_ID] = recipientAddressModel.provinceId
        params[EditAddressParam.LATITUDE] = addressLatitude
        params[EditAddressParam.LONGITUDE] = addressLongitude
        params[EditAddressParam.RECEIVER_NAME] = recipientAddressModel.recipientName
        params[EditAddressParam.RECEIVER_PHONE] = recipientAddressModel.recipientPhoneNumber
        val requestParams = RequestParams.create()
        requestParams.putAllString(params)
        return requestParams
    }

    private fun getActualThrowableForRx(t: Throwable) = t.cause?.cause ?: t.cause ?: t

    suspend fun getRates(
        ratesParam: RatesParam,
        shopShipments: List<ShopShipment>,
        selectedServiceId: Int,
        selectedSpId: Int
    ) {
        withContext(dispatchers.io) {
            try {
                var shippingRecommendationData = ratesUseCase(ratesParam)
                shippingRecommendationData = ratesResponseStateConverter.fillState(
                    shippingRecommendationData,
                    shopShipments,
                    selectedSpId,
                    selectedServiceId
                )
                CheckoutOrderShipment(
                    isLoading = false
                )
            } catch (t: Throwable) {
                Timber.d(t)
            }
        }
    }

    suspend fun getRatesApi(
        ratesParam: RatesParam,
        shopShipments: List<ShopShipment>,
        selectedSpId: Int
    ) {
        withContext(dispatchers.io) {
            try {
                var shippingRecommendationData = ratesApiUseCase(ratesParam)
                shippingRecommendationData = ratesResponseStateConverter.fillState(
                    shippingRecommendationData,
                    shopShipments,
                    selectedSpId,
                    0
                )
                CheckoutOrderShipment(
                    isLoading = false
                )
            } catch (t: Throwable) {
                Timber.d(t)
            }
        }
    }

    suspend fun getRatesWithScheduleDelivery(
        ratesParam: RatesParam,
        shopShipments: List<ShopShipment>,
        selectedServiceId: Int,
        selectedSpId: Int,
        fullfilmentId: String
    ) {
        withContext(dispatchers.io) {
            try {
                var shippingRecommendationData = ratesWithScheduleUseCase(ratesParam to fullfilmentId)
                shippingRecommendationData = ratesResponseStateConverter.fillState(
                    shippingRecommendationData,
                    shopShipments,
                    selectedSpId,
                    0
                )
                CheckoutOrderShipment(
                    isLoading = false
                )
            } catch (t: Throwable) {
                Timber.d(t)
            }
        }
    }
}

data class EditAddressResult(
    val isSuccess: Boolean,
    val errorMessage: String = "",
    val throwable: Throwable? = null
)
