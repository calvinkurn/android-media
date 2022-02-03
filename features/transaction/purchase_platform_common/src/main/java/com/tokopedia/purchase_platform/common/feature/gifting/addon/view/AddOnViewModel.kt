package com.tokopedia.purchase_platform.common.feature.gifting.addon.view

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.purchase_platform.common.feature.gifting.addon.domain.usecase.GetAddOnByProductIdUseCase
import javax.inject.Inject

class AddOnViewModel @Inject constructor(executorDispatchers: CoroutineDispatchers,
                                         private val getAddOnByProductIdUseCase: GetAddOnByProductIdUseCase)
    : BaseViewModel(executorDispatchers.main) {



}