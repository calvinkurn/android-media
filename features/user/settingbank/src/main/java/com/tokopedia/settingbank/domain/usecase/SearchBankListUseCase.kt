package com.tokopedia.settingbank.domain.usecase

import com.tokopedia.settingbank.domain.model.Bank
import com.tokopedia.settingbank.view.viewState.BankListState
import com.tokopedia.settingbank.view.viewState.OnBankSearchResult
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import kotlinx.coroutines.delay
import javax.inject.Inject

class SearchBankListUseCase @Inject constructor() : UseCase<BankListState>() {

    private val DELAY_IN_SEARCH = 200L
    private val PARAM_SEARCH_QUERY = "param_search_query"
    private val PARAM_BANK_LIST = "param_bank_list"


    fun searchForBanks(query: String?, bankList: ArrayList<Bank>?,
                       onSearchComplete: (BankListState) -> Unit) {
        val requestParams = RequestParams().apply {
            putString(PARAM_SEARCH_QUERY, query ?: "")
            putObject(PARAM_BANK_LIST, bankList ?: arrayListOf<Bank>())
        }
        execute({
            onSearchComplete(it)
        }, {
            onSearchComplete(OnBankSearchResult(bankList ?: arrayListOf()))
        }, requestParams)
    }

    override suspend fun executeOnBackground(): BankListState {
        delay(DELAY_IN_SEARCH)
        val query = useCaseRequestParams.getString(PARAM_SEARCH_QUERY, "")
        val bankList = useCaseRequestParams.getObject(PARAM_BANK_LIST) as ArrayList<Bank>
        val searchedList = searchInMasterList(query, bankList)
        return OnBankSearchResult(searchedList)
    }

    private fun searchInMasterList(query: String, bankList: ArrayList<Bank>): ArrayList<Bank> {
        val searchedBankList = arrayListOf<Bank>()
        if (query.isEmpty()) {
            return bankList
        }
        bankList.forEach { bank ->
            bank.bankName?.let {
                if (bank.bankName.contains(query, true)) {
                    searchedBankList.add(bank)
                }
            }
        }
        return searchedBankList
    }

}