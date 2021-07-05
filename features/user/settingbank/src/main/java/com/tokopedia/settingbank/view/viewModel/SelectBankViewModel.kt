package com.tokopedia.settingbank.view.viewModel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.settingbank.domain.model.Bank
import com.tokopedia.settingbank.domain.usecase.BankListUseCase
import com.tokopedia.settingbank.domain.usecase.SearchBankListUseCase
import com.tokopedia.settingbank.view.viewState.BankListState
import com.tokopedia.settingbank.view.viewState.OnBankListLoaded
import com.tokopedia.settingbank.view.viewState.OnBankSearchResult
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject


class SelectBankViewModel @Inject constructor(
        private val bankListUseCase: dagger.Lazy<BankListUseCase>,
        private val searchBankUseCase: dagger.Lazy<SearchBankListUseCase>,
        dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {

    private val masterBankList = MutableLiveData<ArrayList<Bank>>()
    val bankListState = MutableLiveData<BankListState>()

    fun loadBankList() {
        bankListUseCase.get().getBankList {
            when (it) {
                is OnBankListLoaded -> {
                    masterBankList.value = it.bankList
                    bankListState.postValue(it)
                }
                else -> {
                    bankListState.postValue(it)
                }
            }
        }
    }

    fun searchBankByQuery(query: String?) {
        searchBankUseCase.get().cancelJobs()
        searchBankUseCase.get().searchForBanks(
                query, masterBankList.value
        ) {
            bankListState.postValue(OnBankSearchResult(it))
        }
    }

    fun resetSearchResult() {
        searchBankByQuery("")
    }

    override fun onCleared() {
        bankListUseCase.get().cancelJobs()
        searchBankUseCase.get().cancelJobs()
        super.onCleared()
    }

}