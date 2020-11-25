package com.tokopedia.settingbank.view.viewModel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.settingbank.di.QUERY_ADD_BANK_ACCOUNT
import com.tokopedia.settingbank.domain.AddBankRequest
import com.tokopedia.settingbank.domain.RichieAddBankAccountNewFlow
import com.tokopedia.settingbank.view.viewState.*
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

class AddAccountViewModel @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                              private val rawQueries: Map<String, String>,
                                              dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {

    val addAccountState = MutableLiveData<AddAccountState>()

    private var job: Job? = null

    fun addBank(addBankRequest: AddBankRequest) {
        cancelCurrentJob()
        createNewJob()
        if (addAccountState.value == OnAddBankRequestStarted)
            return
        addAccountState.value = OnAddBankRequestStarted
        launchCatchError(block = {
            val params = getAddBankParams(addBankRequest)
            val data = makeAddBankGQL(params, job!!)
            processResponse(data.getSuccessData())
            addAccountState.value = OnAddBankRequestEnded

        }) {
            addAccountState.value = OnAddBankRequestEnded
            addAccountState.value = OnAddAccountNetworkError(it)
            it.printStackTrace()
        }
    }

    private fun processResponse(response: RichieAddBankAccountNewFlow) {
        if (response.response.status == 200) {
            addAccountState.value = OnSuccessfullyAdded(response.response)
        } else {
            addAccountState.value = OnAccountAddingError(Exception(response.response.message))
        }
    }

    private suspend fun makeAddBankGQL(params: Map<String, Any>, job: Job): GraphqlResponse =
            withContext(Dispatchers.IO + job) {
                val graphRequest = GraphqlRequest(rawQueries[QUERY_ADD_BANK_ACCOUNT],
                        RichieAddBankAccountNewFlow::class.java, params)
                graphqlRepository.getReseponse(listOf(graphRequest))
            }

    private fun getAddBankParams(addBankRequest: AddBankRequest): Map<String, Any> = mapOf(
            BANK_ID to addBankRequest.bankId,
            BANK_NAME to addBankRequest.bankName,
            ACCOUNT_NUMBER to addBankRequest.accountNo,
            ACCOUNT_NAME to addBankRequest.accountName,
            IS_MANUAL to addBankRequest.isManual
    )

    companion object {
        const val BANK_ID = "bankID"
        const val BANK_NAME = "bankName"
        const val ACCOUNT_NUMBER = "accountNo"
        const val ACCOUNT_NAME = "accountName"
        const val IS_MANUAL = "isManual"
    }

    private fun createNewJob() {
        job = Job()
    }

    private fun cancelCurrentJob() {
        job?.cancel()
    }

    override fun onCleared() {
        cancelCurrentJob()
        super.onCleared()
    }
}