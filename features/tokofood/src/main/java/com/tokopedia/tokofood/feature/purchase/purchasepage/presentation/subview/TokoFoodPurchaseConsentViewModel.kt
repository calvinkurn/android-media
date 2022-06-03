package com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.subview

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tokofood.common.util.Result
import com.tokopedia.tokofood.feature.purchase.purchasepage.domain.usecase.AgreeConsentUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class TokoFoodPurchaseConsentViewModel @Inject constructor(
    private val agreeConsentUseCase: AgreeConsentUseCase,
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private val _agreeConsentData = MutableSharedFlow<Result<Boolean>>()
    val agreeConsentData = _agreeConsentData.asSharedFlow()

    fun agreeConsent() {
        launchCatchError(block = {
            _agreeConsentData.emit(Result.Loading())
            agreeConsentUseCase(Unit).collect {
                val isSuccess = it.data.tokofoodSubmitUserConsent.isSuccess
                if (isSuccess) {
                    _agreeConsentData.emit(Result.Success(true))
                } else {
                    _agreeConsentData.emit(Result.Failure(MessageErrorException(it.data.tokofoodSubmitUserConsent.message)))
                }
            }
        }, onError = {
            _agreeConsentData.emit(Result.Failure(it))
        })
    }

}