package com.tokopedia.tokofood.stub.purchase.presentation.viewmodel

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.logisticCommon.domain.usecase.GetAddressDetailById
import com.tokopedia.logisticCommon.domain.usecase.UpdatePinpointWithAddressIdUseCase
import com.tokopedia.tokofood.feature.purchase.purchasepage.domain.usecase.CheckoutGeneralTokoFoodUseCase
import com.tokopedia.tokofood.feature.purchase.purchasepage.domain.usecase.CheckoutTokoFoodUseCase
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.TokoFoodPurchaseViewModel
import dagger.Lazy
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

@OptIn(FlowPreview::class)
class TokoFoodPurchaseViewModelStub @Inject constructor(
    keroEditAddressUseCase: Lazy<UpdatePinpointWithAddressIdUseCase>,
    getAddressDetailById: Lazy<GetAddressDetailById>,
    cartListTokofoodUseCase: Lazy<CheckoutTokoFoodUseCase>,
    checkoutGeneralTokoFoodUseCase: Lazy<CheckoutGeneralTokoFoodUseCase>,
    coroutineDispatchers: CoroutineDispatchers
) : TokoFoodPurchaseViewModel(
    keroEditAddressUseCase,
    getAddressDetailById,
    cartListTokofoodUseCase,
    checkoutGeneralTokoFoodUseCase,
    coroutineDispatchers
)
