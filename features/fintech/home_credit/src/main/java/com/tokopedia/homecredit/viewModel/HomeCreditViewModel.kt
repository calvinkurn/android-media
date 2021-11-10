package com.tokopedia.homecredit.viewModel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.homecredit.domain.HomeCreditUseCase
import javax.inject.Inject

class HomeCreditViewModel @Inject constructor(
    private val homeCreditUseCase: HomeCreditUseCase,
    private val dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.io)
