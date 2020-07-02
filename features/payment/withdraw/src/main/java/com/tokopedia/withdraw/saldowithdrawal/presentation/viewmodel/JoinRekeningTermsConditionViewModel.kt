package com.tokopedia.withdraw.saldowithdrawal.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.withdraw.saldowithdrawal.domain.usecase.JoinRekeningTNCUseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class JoinRekeningTermsConditionViewModel @Inject constructor(
        private val useCase: JoinRekeningTNCUseCase,
        dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {

    val tncResponseMutableData = MutableLiveData<Result<String>>()
    private var isRequestInProgress = false

    fun loadJoinRekeningTermsCondition(programID: Int) {
        if (isRequestInProgress)
            return
        isRequestInProgress = true
        launchCatchError(block = {
            when (val result = useCase
                    .loadJoinRekeningTermsCondition(programID)) {
                is Success -> {
                    tncResponseMutableData.postValue(Success(result.data
                            .rekPreTncResponse.rekPreTncResponseData.template))
                }
                is Fail -> {
                    tncResponseMutableData.postValue(result)
                }
            }
            isRequestInProgress = false
        }, onError = {
            tncResponseMutableData.postValue(Fail(it))
            isRequestInProgress = false
        })
    }

}