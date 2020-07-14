package com.tokopedia.settingbank.view.viewModel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.settingbank.di.QUERY_CHECK_BANK_ACCOUNT
import com.tokopedia.settingbank.domain.CheckAccountResponse
import com.tokopedia.settingbank.view.viewState.AccountCheckState
import com.tokopedia.settingbank.view.viewState.OnAccountCheckSuccess
import com.tokopedia.settingbank.view.viewState.OnCheckAccountError
import com.tokopedia.settingbank.view.viewState.OnErrorInAccountNumber
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class CheckAccountNumberViewModel @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                                      private val rawQueries: Map<String, String>,
                                                      dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {

    val KEY_PARAM_ACCOUNT_NUMBER = "accountNumber"
    val KEY_PARAM_BANK_ID = "bankId"

    val accountCheckState = MutableLiveData<AccountCheckState>()

    private var isRequestRunning = false

    fun checkAccountNumber(bankId: Long, accountNumber: String?) {
        if (isRequestRunning)
            return
        launchCatchError(block = {
            isRequestRunning = true
            val response = getAccountCheckState(getAccountCheckParams(bankId, accountNumber!!))
            val state = processAccountCheckResponse(response.getSuccessData())
            accountCheckState.value= state
            isRequestRunning = false
        }) {
            isRequestRunning = false
            accountCheckState.value =  OnCheckAccountError(it)
            it.printStackTrace()
        }
    }

    private suspend fun processAccountCheckResponse(response: CheckAccountResponse): AccountCheckState = withContext(Dispatchers.IO) {
        if(response.checkAccountNumber.successCode == 200){
            return@withContext OnAccountCheckSuccess(response.checkAccountNumber.accountHolderName)
        }
        return@withContext OnErrorInAccountNumber(response.checkAccountNumber.message)
    }

    private suspend fun getAccountCheckState(param: Map<String, Any>): GraphqlResponse = withContext(Dispatchers.IO) {
        val graphRequest = GraphqlRequest(rawQueries[QUERY_CHECK_BANK_ACCOUNT],
                CheckAccountResponse::class.java, param)
        graphqlRepository.getReseponse(listOf(graphRequest))
    }

    private fun getAccountCheckParams(bankId: Long, accountNumber: String) = mapOf(KEY_PARAM_ACCOUNT_NUMBER to accountNumber,
            KEY_PARAM_BANK_ID to bankId)


}