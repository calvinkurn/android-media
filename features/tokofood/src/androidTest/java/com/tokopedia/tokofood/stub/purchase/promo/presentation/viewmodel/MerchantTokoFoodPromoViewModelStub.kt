package com.tokopedia.tokofood.stub.purchase.promo.presentation.viewmodel

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.tokofood.feature.purchase.promopage.domain.usecase.MerchantPromoListTokoFoodUseCase
import com.tokopedia.tokofood.feature.purchase.promopage.presentation.MerchantTokoFoodPromoViewModel
import dagger.Lazy
import javax.inject.Inject

class MerchantTokoFoodPromoViewModelStub @Inject constructor(
    promoListTokoFoodUseCase: Lazy<MerchantPromoListTokoFoodUseCase>,
    dispatcher: CoroutineDispatchers
): MerchantTokoFoodPromoViewModel(
    promoListTokoFoodUseCase, dispatcher
)
