package com.tokopedia.checkout.old.view.subscriber

import com.tokopedia.akamai_bot_lib.exception.AkamaiErrorException
import com.tokopedia.checkout.old.domain.model.cartshipmentform.CartShipmentAddressFormData
import com.tokopedia.checkout.old.domain.model.cartshipmentform.GroupAddress
import com.tokopedia.checkout.old.view.ShipmentContract
import com.tokopedia.checkout.old.view.ShipmentPresenter
import com.tokopedia.logisticCommon.data.entity.address.UserAddress
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.purchase_platform.common.exception.CartResponseErrorException
import rx.Subscriber
import timber.log.Timber

class GetShipmentAddressFormSubscriber(private val shipmentPresenter: ShipmentPresenter,
                                       private val view: ShipmentContract.View,
                                       private val isReloadData: Boolean,
                                       private val isReloadAfterPriceChangeHinger: Boolean,
                                       private val isOneClickShipment: Boolean) : Subscriber<CartShipmentAddressFormData?>() {
    override fun onCompleted() {}

    override fun onError(e: Throwable) {
        Timber.d(e)
        if (isReloadData) {
            view.setHasRunningApiCall(false)
            view.hideLoading()
        } else {
            view.hideInitialLoading()
        }
        var errorMessage = e.message
        if (e !is CartResponseErrorException && e !is AkamaiErrorException) {
            errorMessage = ErrorHandler.getErrorMessage(view.activityContext, e)
        }
        view.showToastError(errorMessage)
        view.stopTrace()
        view.logOnErrorLoadCheckoutPage(e)
    }

    override fun onNext(cartShipmentAddressFormData: CartShipmentAddressFormData?) {
        if (isReloadData) {
            view.setHasRunningApiCall(false)
            view.resetPromoBenefit()
            view.clearTotalBenefitPromoStacking()
            view.hideLoading()
        } else {
            view.hideInitialLoading()
        }

        validateShipmentAddressFormData(cartShipmentAddressFormData)
        view.stopTrace()
    }

    private fun validateShipmentAddressFormData(cartShipmentAddressFormData: CartShipmentAddressFormData?) {
        if (cartShipmentAddressFormData == null) {
            view.onShipmentAddressFormEmpty()
        } else {
            if (cartShipmentAddressFormData.isError) {
                if (cartShipmentAddressFormData.isOpenPrerequisiteSite) {
                    view.onCacheExpired(cartShipmentAddressFormData.errorMessage)
                } else {
                    view.showToastError(cartShipmentAddressFormData.errorMessage)
                    view.logOnErrorLoadCheckoutPage(MessageErrorException(cartShipmentAddressFormData.errorMessage))
                }
            } else {
                val groupAddressList: List<GroupAddress> = cartShipmentAddressFormData.groupAddress
                val userAddress = groupAddressList.firstOrNull()?.userAddress
                validateRenderCheckoutPage(cartShipmentAddressFormData, userAddress)
            }
        }
    }

    private fun validateRenderCheckoutPage(cartShipmentAddressFormData: CartShipmentAddressFormData, userAddress: UserAddress?) {
        when (cartShipmentAddressFormData.errorCode) {
            CartShipmentAddressFormData.ERROR_CODE_TO_OPEN_ADD_NEW_ADDRESS -> {
                view.renderCheckoutPageNoAddress(cartShipmentAddressFormData)
            }
            CartShipmentAddressFormData.ERROR_CODE_TO_OPEN_ADDRESS_LIST -> {
                view.renderCheckoutPageNoMatchedAddress(cartShipmentAddressFormData, userAddress?.state ?: 0)
            }
            CartShipmentAddressFormData.NO_ERROR -> {
                if (userAddress == null) {
                    view.onShipmentAddressFormEmpty()
                } else {
                    view.updateLocalCacheAddressData(userAddress)
                    shipmentPresenter.initializePresenterData(cartShipmentAddressFormData)
                    view.renderCheckoutPage(!isReloadData, isReloadAfterPriceChangeHinger, isOneClickShipment)
                    if (cartShipmentAddressFormData.popUpMessage.isNotEmpty()) {
                        view.showToastNormal(cartShipmentAddressFormData.popUpMessage)
                    }
                }
            }
        }
    }

}