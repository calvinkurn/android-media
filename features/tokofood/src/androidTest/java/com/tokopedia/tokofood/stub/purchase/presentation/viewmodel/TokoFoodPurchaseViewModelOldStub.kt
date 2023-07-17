package com.tokopedia.tokofood.stub.purchase.presentation.viewmodel

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.tokofood.common.domain.usecase.KeroEditAddressUseCase
import com.tokopedia.tokofood.common.domain.usecase.KeroGetAddressUseCase
import com.tokopedia.tokofood.feature.purchase.purchasepage.domain.usecase.CheckoutGeneralTokoFoodUseCaseOld
import com.tokopedia.tokofood.feature.purchase.purchasepage.domain.usecase.CheckoutTokoFoodUseCaseOld
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.TokoFoodPurchaseViewModelOld
import dagger.Lazy
import javax.inject.Inject

class TokoFoodPurchaseViewModelOldStub @Inject constructor(
    keroEditAddressUseCase: Lazy<KeroEditAddressUseCase>,
    keroGetAddressUseCase: Lazy<KeroGetAddressUseCase>,
    checkoutTokoFoodUseCase: Lazy<CheckoutTokoFoodUseCaseOld>,
    checkoutGeneralTokoFoodUseCase: Lazy<CheckoutGeneralTokoFoodUseCaseOld>,
    dispatcher: CoroutineDispatchers
): TokoFoodPurchaseViewModelOld(
    keroEditAddressUseCase,
    keroGetAddressUseCase,
    checkoutTokoFoodUseCase,
    checkoutGeneralTokoFoodUseCase,
    dispatcher
)
