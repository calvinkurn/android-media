package com.tokopedia.deals.pdp.ui.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import javax.inject.Inject

class DealsPDPViewModel @Inject constructor (
    dispatcher: CoroutineDispatchers
): BaseViewModel(dispatcher.main) {

}