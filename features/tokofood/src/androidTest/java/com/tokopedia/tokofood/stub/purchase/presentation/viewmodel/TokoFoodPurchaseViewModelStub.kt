package com.tokopedia.tokofood.stub.purchase.presentation.viewmodel

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.tokofood.common.domain.usecase.KeroEditAddressUseCase
import com.tokopedia.tokofood.common.domain.usecase.KeroGetAddressUseCase
import com.tokopedia.tokofood.feature.purchase.purchasepage.domain.usecase.CheckoutGeneralTokoFoodUseCase
import com.tokopedia.tokofood.feature.purchase.purchasepage.domain.usecase.CheckoutTokoFoodUseCase
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.TokoFoodPurchaseViewModel
import dagger.Lazy
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

@OptIn(FlowPreview::class)
class TokoFoodPurchaseViewModelStub @Inject constructor(
    keroEditAddressUseCase: Lazy<KeroEditAddressUseCase>,
    keroGetAddressUseCase: Lazy<KeroGetAddressUseCase>,
    cartListTokofoodUseCase: Lazy<CheckoutTokoFoodUseCase>,
    checkoutGeneralTokoFoodUseCase: Lazy<CheckoutGeneralTokoFoodUseCase>,
    coroutineDispatchers: CoroutineDispatchers
): TokoFoodPurchaseViewModel(
    keroEditAddressUseCase,
    keroGetAddressUseCase,
    cartListTokofoodUseCase,
    checkoutGeneralTokoFoodUseCase,
    coroutineDispatchers
)
