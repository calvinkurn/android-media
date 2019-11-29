package com.tokopedia.settingbank.banklist.v2.view.viewModel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.settingbank.banklist.v2.di.QUERY_GET_USER_BANK_ACCOUNTS
import com.tokopedia.settingbank.banklist.v2.domain.BankAccountListResponse
import com.tokopedia.settingbank.banklist.v2.view.viewState.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SettingBankViewModel @Inject constructor(val graphqlRepository: GraphqlRepository,
                                               private val rawQueries: Map<String, String>,
                                               dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {

    val getBankListState = MutableLiveData<GetBankAccountListState>()

    internal fun loadUserAddedBankList() {
        launchCatchError(block = {
            getBankListState.value = OnShowLoading(true)
            val data = withContext(Dispatchers.IO) {
                val graphRequest = GraphqlRequest(rawQueries[QUERY_GET_USER_BANK_ACCOUNTS],
                        BankAccountListResponse::class.java)
                graphqlRepository.getReseponse(listOf(graphRequest))
            }
            getBankListState.value = OnShowLoading(false)
            processUserBankData(data.getSuccessData())
        }) {
            it.printStackTrace()
        }
    }

    private fun processUserBankData(bankAccountListResponse: BankAccountListResponse) {
        bankAccountListResponse.getBankAccount.data.bankAccount?.let {
            val toastMessage = bankAccountListResponse.getBankAccount.data.userInfo.message
            if (it.isNotEmpty()) {
                getBankListState.value = OnBankAccountListLoaded(it, toastMessage ?: "")
            } else {
                getBankListState.value = NoBankAccountAdded(toastMessage ?: "")
            }
        }
    }
}
