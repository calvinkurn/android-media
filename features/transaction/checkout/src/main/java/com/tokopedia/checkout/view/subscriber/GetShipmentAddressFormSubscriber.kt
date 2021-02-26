package com.tokopedia.checkout.view.subscriber

import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.checkout.domain.model.cartshipmentform.CartShipmentAddressFormData
import com.tokopedia.checkout.domain.model.cartshipmentform.GroupAddress
import com.tokopedia.checkout.view.ShipmentContract
import com.tokopedia.checkout.view.ShipmentPresenter
import com.tokopedia.logisticCommon.data.entity.address.UserAddress
import com.tokopedia.purchase_platform.common.exception.CartResponseErrorException
import com.tokopedia.purchase_platform.common.utils.isNullOrEmpty
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
        if (e !is CartResponseErrorException) {
            errorMessage = ErrorHandler.getErrorMessage(view.activityContext, e)
        }
        view.showToastError(errorMessage)
        view.stopTrace()
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

        if (cartShipmentAddressFormData == null) {
            view.onShipmentAddressFormEmpty()
        } else {
            if (cartShipmentAddressFormData.isError) {
                if (cartShipmentAddressFormData.isOpenPrerequisiteSite) {
                    view.onCacheExpired(cartShipmentAddressFormData.errorMessage)
                } else {
                    view.showToastError(cartShipmentAddressFormData.errorMessage)
                }
            } else {
                val groupAddressList: List<GroupAddress?> = cartShipmentAddressFormData.groupAddress
                if (cartShipmentAddressFormData.errorCode == 0 && (groupAddressList.isEmpty() || groupAddressList[0] == null || groupAddressList[0]?.userAddress == null)) {
                    view.onShipmentAddressFormEmpty()
                } else {
                    val userAddress = groupAddressList[0]?.userAddress
                    if (userAddress == null) {
                        view.onShipmentAddressFormEmpty()
                    } else {
                        validateRenderCheckoutPage(cartShipmentAddressFormData, userAddress)
                    }
                }
            }
        }
        view.stopTrace()
    }

    private fun validateRenderCheckoutPage(cartShipmentAddressFormData: CartShipmentAddressFormData, userAddress: UserAddress) {
        when (cartShipmentAddressFormData.errorCode) {
            CartShipmentAddressFormData.ERROR_CODE_TO_OPEN_ADD_NEW_ADDRESS -> {
                view.renderCheckoutPageNoAddress(cartShipmentAddressFormData)
            }
            CartShipmentAddressFormData.ERROR_CODE_TO_OPEN_ADDRESS_LIST -> {
                view.renderCheckoutPageNoMatchedAddress(cartShipmentAddressFormData, userAddress.state)
            }
            else -> {
                if (userAddress.state == UserAddress.STATE_ADDRESS_ID_NOT_MATCH) {
                    shipmentPresenter.initializePresenterData(cartShipmentAddressFormData)
                    view.renderCheckoutPage(!isReloadData, isReloadAfterPriceChangeHinger, isOneClickShipment)
                    if (!isNullOrEmpty(cartShipmentAddressFormData.popUpMessage)) {
                        view.showToastNormal(cartShipmentAddressFormData.popUpMessage)
                    }
                    view.updateLocalCacheAddressData(userAddress)
                } else {
                    shipmentPresenter.initializePresenterData(cartShipmentAddressFormData)
                    view.renderCheckoutPage(!isReloadData, isReloadAfterPriceChangeHinger, isOneClickShipment)
                }
            }
        }
    }

}