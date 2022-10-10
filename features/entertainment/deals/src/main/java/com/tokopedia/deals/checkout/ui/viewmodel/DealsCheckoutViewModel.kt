package com.tokopedia.deals.checkout.ui.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import javax.inject.Inject

class DealsCheckoutViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

}
