package com.tokopedia.checkout.revamp.view.processor

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.checkout.domain.model.cartshipmentform.CartShipmentAddressFormData
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutPageState
import com.tokopedia.logisticCommon.data.constant.AddressConstant
import com.tokopedia.logisticCommon.data.response.KeroAddrIsEligibleForAddressFeatureData
import com.tokopedia.logisticCommon.domain.usecase.EditAddressUseCase
import com.tokopedia.logisticCommon.domain.usecase.EligibleForAddressUseCase
import com.tokopedia.logisticcart.scheduledelivery.domain.usecase.GetRatesWithScheduleUseCase
import com.tokopedia.logisticcart.shipping.usecase.GetRatesApiUseCase
import com.tokopedia.logisticcart.shipping.usecase.GetRatesUseCase
import dagger.Lazy
import javax.inject.Inject

class CheckoutLogisticProcessor @Inject constructor(
    private val eligibleForAddressUseCase: Lazy<EligibleForAddressUseCase>,
    private val editAddressUseCase: EditAddressUseCase,
    private val ratesUseCase: GetRatesUseCase,
    private val ratesApiUseCase: GetRatesApiUseCase,
    private val ratesWithScheduleUseCase: GetRatesWithScheduleUseCase,
    private val dispatchers: CoroutineDispatchers
) {

    fun checkIsUserEligibleForRevampAna(cartShipmentAddressFormData: CartShipmentAddressFormData, callback: (CheckoutPageState) -> Unit) {
            eligibleForAddressUseCase.get()
                .eligibleForAddressFeature({ response: KeroAddrIsEligibleForAddressFeatureData ->
                    callback(CheckoutPageState.NoAddress(
                        cartShipmentAddressFormData,
                        response.eligibleForRevampAna.eligible
                    ))
                }, { throwable: Throwable ->
                    var errorMessage = throwable.message
//            if (errorMessage == null) {
//                errorMessage =
//                    view?.getStringResource(com.tokopedia.abstraction.R.string.default_request_error_unknown_short)
//            }
//            view?.showToastError(errorMessage)
                    callback(CheckoutPageState.Error(throwable))
                }, AddressConstant.ANA_REVAMP_FEATURE_ID)
    }
}
