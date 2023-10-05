package com.tokopedia.tokofood.stub.purchase.promo.presentation.viewmodel

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.tokofood.feature.purchase.promopage.domain.usecase.PromoListTokoFoodUseCase
import com.tokopedia.tokofood.feature.purchase.promopage.presentation.TokoFoodPromoViewModel
import dagger.Lazy
import javax.inject.Inject

class TokoFoodPromoViewModelStub @Inject constructor(
    promoListTokoFoodUseCase: Lazy<PromoListTokoFoodUseCase>,
    dispatcher: CoroutineDispatchers
): TokoFoodPromoViewModel(
    promoListTokoFoodUseCase, dispatcher
)
