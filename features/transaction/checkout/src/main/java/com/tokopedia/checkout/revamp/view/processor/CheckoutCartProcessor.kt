package com.tokopedia.checkout.revamp.view.processor

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.checkout.data.model.request.saf.ShipmentAddressFormRequest
import com.tokopedia.checkout.domain.model.cartshipmentform.CartShipmentAddressFormData
import com.tokopedia.checkout.domain.usecase.ChangeShippingAddressGqlUseCase
import com.tokopedia.checkout.domain.usecase.GetShipmentAddressFormV4UseCase
import com.tokopedia.checkout.domain.usecase.ReleaseBookingUseCase
import com.tokopedia.checkout.domain.usecase.SaveShipmentStateGqlUseCase
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutPageState
import com.tokopedia.logisticCommon.data.entity.address.UserAddress
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.purchase_platform.common.feature.dynamicdatapassing.domain.UpdateDynamicDataPassingUseCase
import dagger.Lazy
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class CheckoutCartProcessor @Inject constructor(
    private val getShipmentAddressFormV4UseCase: GetShipmentAddressFormV4UseCase,
    private val saveShipmentStateGqlUseCase: SaveShipmentStateGqlUseCase,
    private val changeShippingAddressGqlUseCase: Lazy<ChangeShippingAddressGqlUseCase>,
    private val updateDynamicDataPassingUseCase: UpdateDynamicDataPassingUseCase,
    private val releaseBookingUseCase: Lazy<ReleaseBookingUseCase>,
    private val dispatchers: CoroutineDispatchers
) {

    suspend fun hitSAF(
        isOneClickShipment: Boolean,
        isTradeIn: Boolean,
        skipUpdateOnboardingState: Boolean,
        cornerId: String?,
        deviceId: String?,
        checkoutLeasingId: String?,
        isPlusSelected: Boolean,
        isReloadData: Boolean,
        isReloadAfterPriceChangeHigher: Boolean
    ): CheckoutPageState {
        return withContext(dispatchers.io) {
            try {
                val cartShipmentAddressFormData = getShipmentAddressFormV4UseCase(
                    ShipmentAddressFormRequest(
                        isOneClickShipment,
                        isTradeIn,
                        skipUpdateOnboardingState,
                        cornerId,
                        deviceId,
                        checkoutLeasingId,
                        isPlusSelected
                    )
                )
                validateShipmentAddressFormData(
                    cartShipmentAddressFormData,
                    isReloadData,
                    isReloadAfterPriceChangeHigher,
                    isOneClickShipment
                )
            } catch (t: Throwable) {
                Timber.d(t)
                CheckoutPageState.Error(t)
            }
        }
    }

    private fun validateShipmentAddressFormData(
        cartShipmentAddressFormData: CartShipmentAddressFormData,
        isReloadData: Boolean,
        isReloadAfterPriceChangeHigher: Boolean,
        isOneClickShipment: Boolean
    ): CheckoutPageState {
        if (cartShipmentAddressFormData.isError) {
            if (cartShipmentAddressFormData.isOpenPrerequisiteSite) {
                return CheckoutPageState.CacheExpired(cartShipmentAddressFormData.errorMessage)
            } else {
                return CheckoutPageState.Error(
                    MessageErrorException(
                        cartShipmentAddressFormData.errorMessage
                    ),
                    true
                )
//                view?.showToastError(cartShipmentAddressFormData.errorMessage)
//                view?.logOnErrorLoadCheckoutPage(
//                    MessageErrorException(
//                        cartShipmentAddressFormData.errorMessage
//                    )
//                )
            }
        } else {
            val groupAddressList = cartShipmentAddressFormData.groupAddress
            val userAddress = groupAddressList.firstOrNull()?.userAddress
            return validateRenderCheckoutPage(
                cartShipmentAddressFormData,
                userAddress,
                isReloadData,
                isReloadAfterPriceChangeHigher,
                isOneClickShipment
            )
        }
    }

    private fun validateRenderCheckoutPage(
        cartShipmentAddressFormData: CartShipmentAddressFormData,
        userAddress: UserAddress?,
        isReloadData: Boolean,
        isReloadAfterPriceChangeHigher: Boolean,
        isOneClickShipment: Boolean
    ): CheckoutPageState {
        if (cartShipmentAddressFormData.errorCode == CartShipmentAddressFormData.ERROR_CODE_TO_OPEN_ADD_NEW_ADDRESS) {
            return CheckoutPageState.CheckNoAddress(cartShipmentAddressFormData)
//            checkIsUserEligibleForRevampAna(cartShipmentAddressFormData)
        } else if (cartShipmentAddressFormData.errorCode == CartShipmentAddressFormData.ERROR_CODE_TO_OPEN_ADDRESS_LIST) {
            return CheckoutPageState.NoMatchedAddress(
//            view?.renderCheckoutPageNoMatchedAddress(
                userAddress?.state ?: 0
            )
        } else if (cartShipmentAddressFormData.errorCode == CartShipmentAddressFormData.NO_ERROR) {
            if (userAddress == null) {
                return CheckoutPageState.EmptyData
//                view?.onShipmentAddressFormEmpty()
            } else {
//                view?.updateLocalCacheAddressData(userAddress)
//                initializePresenterData(cartShipmentAddressFormData)
//                setCurrentDynamicDataParamFromSAF(cartShipmentAddressFormData, isOneClickShipment)
//                view?.renderCheckoutPage(
//                    !isReloadData,
//                    isReloadAfterPriceChangeHigher
//                )
                if (cartShipmentAddressFormData.popUpMessage.isNotEmpty()) {
//                    view?.showToastNormal(cartShipmentAddressFormData.popUpMessage)
                }
                val popUpData = cartShipmentAddressFormData.popup
                if (popUpData.title.isNotEmpty() && popUpData.description.isNotEmpty()) {
//                    view?.showPopUp(popUpData)
                }
                return CheckoutPageState.Success(cartShipmentAddressFormData)
            }
        } else {
            return CheckoutPageState.Error(
                MessageErrorException(
                    cartShipmentAddressFormData.errorMessage
                ),
                true
            )
        }
//        isUsingDdp = cartShipmentAddressFormData.isUsingDdp
//        dynamicData = cartShipmentAddressFormData.dynamicData
//        shipmentPlatformFeeData = cartShipmentAddressFormData.shipmentPlatformFee
//        listSummaryAddOnModel = ShipmentAddOnProductServiceMapper.mapSummaryAddOns(cartShipmentAddressFormData)
    }
}
