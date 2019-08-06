package com.tokopedia.power_merchant.subscribe.view.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.power_merchant.subscribe.domain.interactor.GetPMCancellationQuestionnaireUseCase
import com.tokopedia.power_merchant.subscribe.domain.interactor.SendPMCancellationQuestionnaireUseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ViewModel @Inject constructor(
        val getPMCancellationQuestionnaireUseCase: GetPMCancellationQuestionnaireUseCase,
        val sendPMCancellationQuestionnaireUseCase: SendPMCancellationQuestionnaireUseCase,
        dispatcher: CoroutineDispatcher
): BaseViewModel(dispatcher) {
}