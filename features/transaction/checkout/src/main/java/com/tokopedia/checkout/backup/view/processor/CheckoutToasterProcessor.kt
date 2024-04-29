package com.tokopedia.checkout.backup.view.processor

import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.checkout.backup.view.uimodel.CheckoutPageToaster
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject

@ActivityScope
class CheckoutToasterProcessor @Inject constructor() {

    internal val commonToaster: MutableSharedFlow<CheckoutPageToaster> = MutableSharedFlow()
}
