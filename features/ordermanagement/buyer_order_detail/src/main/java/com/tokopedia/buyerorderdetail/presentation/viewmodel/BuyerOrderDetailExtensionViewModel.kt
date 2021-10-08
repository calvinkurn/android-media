package com.tokopedia.buyerorderdetail.presentation.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import javax.inject.Inject

class BuyerOrderDetailExtensionViewModel @Inject constructor(
    dispatcher: CoroutineDispatchers
): BaseViewModel(dispatcher.main) {


}