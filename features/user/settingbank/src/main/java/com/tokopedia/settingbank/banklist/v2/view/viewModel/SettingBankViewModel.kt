package com.tokopedia.settingbank.banklist.v2.view.viewModel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.settingbank.banklist.v2.di.QUERY_GET_USER_BANK_ACCOUNTS
import com.tokopedia.settingbank.banklist.v2.domain.BankAccount
import com.tokopedia.settingbank.banklist.v2.domain.BankAccountListResponse
import com.tokopedia.settingbank.banklist.v2.domain.UserInfo
import com.tokopedia.settingbank.banklist.v2.view.viewState.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SettingBankViewModel @Inject constructor(val graphqlRepository: GraphqlRepository,
                                               private val rawQueries: Map<String, String>,
                                               dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {

    val getBankListState = MutableLiveData<GetBankAccountListState>()

    val addNewBankAccountState = MutableLiveData<Boolean>()

    internal fun loadUserAddedBankList() {
        launchCatchError(block = {
            getBankListState.value = OnShowLoading(true)
            val data = withContext(Dispatchers.IO) {
                val graphRequest = GraphqlRequest(rawQueries[QUERY_GET_USER_BANK_ACCOUNTS],
                        BankAccountListResponse::class.java)
                graphqlRepository.getReseponse(listOf(graphRequest))
            }
            getBankListState.value = OnShowLoading(false)
            processUserBankAccountData(data.getSuccessData())
        }) {
            getBankListState.value = BankAccountListLoadingError(it)
            it.printStackTrace()
        }
    }


    private fun processUserBankAccountData(bankAccountListResponse: BankAccountListResponse) {
        bankAccountListResponse.getBankAccount.data.bankAccount?.let {
            val toastMessage = bankAccountListResponse.getBankAccount.data.userInfo.message
            if (it.isNotEmpty()) {
                getBankListState.value = OnBankAccountListLoaded(it, toastMessage ?: "")
            } else {
                getBankListState.value = NoBankAccountAdded(toastMessage ?: "")
            }
            updateAddNewBankAccountState(it,bankAccountListResponse.getBankAccount.data.userInfo)
        }
    }

    private fun updateAddNewBankAccountState(bankAccountList: List<BankAccount>,userInfo: UserInfo) {
        if (bankAccountList.isNotEmpty()) {
            launchCatchError(block = {
                val isEnable = withContext(Dispatchers.IO) {
                    return@withContext userInfo.isVerified
                }
                addNewBankAccountState.value = isEnable
            }) {
                addNewBankAccountState.value = false
            }

        } else {
            addNewBankAccountState.value = true
        }
    }
}