package com.tokopedia.addongifting.view

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.addongifting.domain.usecase.GetAddOnByProductUseCase
import javax.inject.Inject

class AddOnViewModel @Inject constructor(executorDispatchers: CoroutineDispatchers,
                                         private val getAddOnByProductUseCase: GetAddOnByProductUseCase)
    : BaseViewModel(executorDispatchers.main) {



}