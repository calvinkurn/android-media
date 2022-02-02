package com.tokopedia.purchase_platform.common.feature.addonbottomsheet.view

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.purchase_platform.common.feature.addonbottomsheet.domain.usecase.GetAddOnByProductIdUseCase
import javax.inject.Inject

class AddOnViewModel @Inject constructor(executorDispatchers: CoroutineDispatchers,
                                         private val getAddOnByProductIdUseCase: GetAddOnByProductIdUseCase)
    : BaseViewModel(executorDispatchers.main) {



}