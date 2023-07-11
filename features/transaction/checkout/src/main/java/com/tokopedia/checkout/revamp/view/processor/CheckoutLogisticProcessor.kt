package com.tokopedia.checkout.revamp.view.processor

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.logisticCommon.domain.usecase.EditAddressUseCase
import com.tokopedia.logisticCommon.domain.usecase.EligibleForAddressUseCase
import com.tokopedia.logisticcart.scheduledelivery.domain.usecase.GetRatesWithScheduleUseCase
import com.tokopedia.logisticcart.shipping.usecase.GetRatesApiUseCase
import com.tokopedia.logisticcart.shipping.usecase.GetRatesUseCase
import javax.inject.Inject

class CheckoutLogisticProcessor @Inject constructor(
    private val eligibleForAddressUseCase: EligibleForAddressUseCase,
    private val editAddressUseCase: EditAddressUseCase,
    private val ratesUseCase: GetRatesUseCase,
    private val ratesApiUseCase: GetRatesApiUseCase,
    private val ratesWithScheduleUseCase: GetRatesWithScheduleUseCase,
    private val dispatchers: CoroutineDispatchers
)
