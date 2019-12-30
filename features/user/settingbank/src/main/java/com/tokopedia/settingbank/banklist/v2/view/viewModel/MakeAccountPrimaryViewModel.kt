package com.tokopedia.settingbank.banklist.v2.view.viewModel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.settingbank.banklist.v2.di.QUERY_MAKE_ACCOUNT_PRIMARY
import com.tokopedia.settingbank.banklist.v2.domain.BankAccount
import com.tokopedia.settingbank.banklist.v2.domain.RichieSetPrimaryBankAccount
import com.tokopedia.settingbank.banklist.v2.view.viewState.*
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MakeAccountPrimaryViewModel @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                                      private val rawQueries: Map<String, String>,
                                                      dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {

    val makeAccountPrimaryState = MutableLiveData<MakePrimaryAccountResponseState>()

    private var job: Job? = null

    fun createBankAccountPrimary(bankAccount: BankAccount) {
        cancelCurrentJob()
        createNewJob()
        if (makeAccountPrimaryState.value == OnMakePrimaryRequestStarted)
            return
        launchCatchError(block = {
            makeAccountPrimaryState.value = OnMakePrimaryRequestStarted
            val params = getParams(bankAccount)
            val data = makeAccountPrimary(params, job!!)
            processResponse(data.getSuccessData())
            makeAccountPrimaryState.value = OnMakePrimaryRequestEnded
        }) {
            makeAccountPrimaryState.value = OnMakePrimaryRequestEnded
            makeAccountPrimaryState.value = OnMakePrimaryRequestError(it)
            it.printStackTrace()
        }
    }

    private fun processResponse(richieSetPrimaryBankAccount: RichieSetPrimaryBankAccount) {
        richieSetPrimaryBankAccount.richieSetPrimaryBankAccount.data?.let { data ->
            when (data.isSuccess) {
                true -> makeAccountPrimaryState.value = OnMakePrimaryRequestSuccess(data.messages)
                else -> makeAccountPrimaryState.value = OnMakePrimaryRequestError(Exception(data.messages))
            }
        }
    }

    private suspend fun makeAccountPrimary(params: Map<String, Any>, job: Job): GraphqlResponse =
            withContext(Dispatchers.IO + job) {
                val graphRequest = GraphqlRequest(rawQueries[QUERY_MAKE_ACCOUNT_PRIMARY],
                        RichieSetPrimaryBankAccount::class.java, params)
                graphqlRepository.getReseponse(listOf(graphRequest))
            }

    private fun getParams(bankAccount: BankAccount): Map<String, Any> = mapOf(
            ACCOUNT_ID to bankAccount.accID
    )

    companion object {
        const val ACCOUNT_ID = "AccountID"
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