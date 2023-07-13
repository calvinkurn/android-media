package com.tokopedia.tokofood.stub.purchase.promo.presentation.viewmodel

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.tokofood.feature.purchase.promopage.domain.usecase.PromoListTokoFoodUseCaseOld
import com.tokopedia.tokofood.feature.purchase.promopage.presentation.TokoFoodPromoViewModelOld
import dagger.Lazy
import javax.inject.Inject

class TokoFoodPromoViewModelOldStub @Inject constructor(
    promoListTokoFoodUseCase: Lazy<PromoListTokoFoodUseCaseOld>,
    dispatcher: CoroutineDispatchers
): TokoFoodPromoViewModelOld(
    promoListTokoFoodUseCase, dispatcher
)
