package com.tokopedia.settingbank.banklist.v2.view.viewModel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.settingbank.banklist.v2.di.QUERY_DELETE_BANK_ACCOUNT
import com.tokopedia.settingbank.banklist.v2.domain.BankAccount
import com.tokopedia.settingbank.banklist.v2.domain.DeleteBankAccountResponse
import com.tokopedia.settingbank.banklist.v2.view.viewState.*
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DeleteBankAccountViewModel @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                                     private val rawQueries: Map<String, String>,
                                                     dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {

    val deleteAccountState = MutableLiveData<DeleteAccountState>()

    private var job: Job? = null

    fun deleteAccount(bankAccount: BankAccount) {
        cancelCurrentJob()
        createNewJob()
        if (deleteAccountState.value == OnDeleteAccountRequestStarted)
            return
        launchCatchError(block = {
            deleteAccountState.value = OnDeleteAccountRequestStarted
            val params = getParams(bankAccount)
            val data = makeAccountPrimary(params, job!!)
            processResponse(data.getSuccessData())
            deleteAccountState.value = OnDeleteAccountRequestEnded
        }) {
            deleteAccountState.value = OnDeleteAccountRequestEnded
            deleteAccountState.value = OnDeleteAccountRequestFailed(it)
            it.printStackTrace()
        }
    }

    private fun processResponse(deleteBankAccountResponse: DeleteBankAccountResponse) {
        deleteBankAccountResponse.deleteBankAccount.data?.let { data ->
            when (data.isSuccess) {
                true -> deleteAccountState.value = OnDeleteAccountRequestSuccess(data.messages)
                else -> deleteAccountState.value = OnDeleteAccountRequestFailed(Exception(data.messages))
            }
        }
    }

    private suspend fun makeAccountPrimary(params: Map<String, Any>, job: Job): GraphqlResponse =
            withContext(Dispatchers.IO + job) {
                val graphRequest = GraphqlRequest(rawQueries[QUERY_DELETE_BANK_ACCOUNT],
                        DeleteBankAccountResponse::class.java, params)
                graphqlRepository.getReseponse(listOf(graphRequest))
            }

    private fun getParams(bankAccount: BankAccount): Map<String, Any> = mapOf(
            ACCOUNT_ID to bankAccount.accID
    )

    companion object {
        const val ACCOUNT_ID = "accID"
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