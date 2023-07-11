package com.tokopedia.checkout.revamp.view.processor

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.checkout.data.model.request.saf.ShipmentAddressFormRequest
import com.tokopedia.checkout.domain.model.cartshipmentform.CartShipmentAddressFormData
import com.tokopedia.checkout.domain.usecase.ChangeShippingAddressGqlUseCase
import com.tokopedia.checkout.domain.usecase.GetShipmentAddressFormV4UseCase
import com.tokopedia.checkout.domain.usecase.ReleaseBookingUseCase
import com.tokopedia.checkout.domain.usecase.SaveShipmentStateGqlUseCase
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
        isPlusSelected: Boolean
    ): CartShipmentAddressFormData? {
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
                cartShipmentAddressFormData
            } catch (t: Throwable) {
                Timber.d(t)
                null
            }
        }
    }
}
