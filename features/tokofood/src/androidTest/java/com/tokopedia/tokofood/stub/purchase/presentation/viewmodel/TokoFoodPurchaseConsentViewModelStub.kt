package com.tokopedia.tokofood.stub.purchase.presentation.viewmodel

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.tokofood.feature.purchase.purchasepage.domain.usecase.AgreeConsentUseCase
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.subview.TokoFoodPurchaseConsentViewModel
import dagger.Lazy
import javax.inject.Inject

class TokoFoodPurchaseConsentViewModelStub @Inject constructor(
    agreeConsentUseCase: Lazy<AgreeConsentUseCase>,
    dispatchers: CoroutineDispatchers
): TokoFoodPurchaseConsentViewModel(agreeConsentUseCase, dispatchers)
