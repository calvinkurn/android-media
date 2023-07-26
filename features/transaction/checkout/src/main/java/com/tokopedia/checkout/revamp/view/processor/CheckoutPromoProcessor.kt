package com.tokopedia.checkout.revamp.view.processor

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.ClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.ValidateUsePromoRevampUseCase
import javax.inject.Inject

class CheckoutPromoProcessor @Inject constructor(
    private val clearCacheAutoApplyStackUseCase: ClearCacheAutoApplyStackUseCase,
    private val validateUsePromoRevampUseCase: ValidateUsePromoRevampUseCase,
    private val dispatchers: CoroutineDispatchers
) {


}
