package com.tokopedia.checkout.revamp.view.processor

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.checkout.domain.usecase.CheckoutUseCase
import javax.inject.Inject

class CheckoutProcessor @Inject constructor(
    private val checkoutGqlUseCase: CheckoutUseCase,
    private val dispatchers: CoroutineDispatchers
) {


}
