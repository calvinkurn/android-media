package com.tokopedia.addongifting.view

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.addongifting.domain.usecase.GetAddOnByProductIdUseCase
import javax.inject.Inject

class AddOnViewModel @Inject constructor(executorDispatchers: CoroutineDispatchers,
                                         private val getAddOnByProductIdUseCase: GetAddOnByProductIdUseCase)
    : BaseViewModel(executorDispatchers.main) {



}