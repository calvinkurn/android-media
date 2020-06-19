package com.tokopedia.withdraw.saldowithdrawal.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.withdraw.saldowithdrawal.domain.helper.WithdrawalDomainConstant.GQL_JOIN_REKE_PREM_TNC_QUERY
import com.tokopedia.withdraw.saldowithdrawal.domain.model.GqlRekPreTncResponse
import com.tokopedia.withdraw.saldowithdrawal.domain.usecase.SaldoGQLUseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Named

class JoinRekeningTermsConditionViewModel @Inject constructor(
        @Named(GQL_JOIN_REKE_PREM_TNC_QUERY) private val query: String,
        private val getJoinRekPreTncUseCase: SaldoGQLUseCase<GqlRekPreTncResponse>,
        dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {

    val tncResponseMutableData = MutableLiveData<Result<String>>()
    private var isRequestInProgress = false

    fun loadJoinRekeningTermsCondition(programID: Int) {
        if (isRequestInProgress)
            return
        isRequestInProgress = true
        launchCatchError(block = {
            val params = mapOf<String, Any?>(
                    PARAM_IS_QUIT to false,
                    PARAM_PROGRAM_ID to programID
            )
            getJoinRekPreTncUseCase.setRequestParams(params)
            getJoinRekPreTncUseCase.setGraphqlQuery(query)
            getJoinRekPreTncUseCase.setTypeClass(GqlRekPreTncResponse::class.java)
            when (val result = getJoinRekPreTncUseCase.executeUseCase()) {
                is Success -> {
                    tncResponseMutableData.postValue(Success(result.data.rekPreTncResponse.rekPreTncResponseData.template))
                }
                is Fail -> {
                    tncResponseMutableData.postValue(result)
                }
            }
            isRequestInProgress = false
        }, onError = {
            isRequestInProgress = false
            tncResponseMutableData.postValue(Fail(it))
        })
    }

    companion object {
        const val PARAM_PROGRAM_ID = "programID"
        const val PARAM_IS_QUIT = "isQuit"
    }
}