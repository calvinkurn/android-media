package com.tokopedia.checkout.revamp.view.processor

import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutPageToaster
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject

@ActivityScope
class CheckoutToasterProcessor @Inject constructor() {

    internal val commonToaster: MutableSharedFlow<CheckoutPageToaster> = MutableSharedFlow()
}
