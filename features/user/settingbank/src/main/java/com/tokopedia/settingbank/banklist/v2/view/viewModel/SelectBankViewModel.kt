package com.tokopedia.settingbank.banklist.v2.view.viewModel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.settingbank.banklist.v2.di.QUERY_GET_BANK_LIST
import com.tokopedia.settingbank.banklist.v2.domain.Bank
import com.tokopedia.settingbank.banklist.v2.domain.GetBankListResponse
import com.tokopedia.settingbank.banklist.v2.view.viewState.*
import kotlinx.coroutines.*
import javax.inject.Inject

const val PARAM_CURRENT_PAGE = "page"
const val PARAM_ITEM_PER_PAGE = "perPage"

class SelectBankViewModel @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                              private val rawQueries: Map<String, String>,
                                              dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {
    private val DELAY_IN_SEARCH = 200L
    private val masterBankList = MutableLiveData<ArrayList<Bank>>()
    val bankListState = MutableLiveData<BankListState>()

    private var currentPage: Int = 1
    private var maxItemPerPage = 999
    private var searchBankJob = Job()

    fun loadBankList() {
        launchCatchError(block = {
            bankListState.value = OnBankListLoading
            val response = loadBankListGQL(getBankListParams())
            processLoadedBankList(response.getSuccessData())
        }) {
            it.message?.let { message ->
                bankListState.value = OnBankListLoadingError(message)
            } ?: run {
                bankListState.value = OnBankListLoadingError("Error")
            }
            it.printStackTrace()
        }
    }

    private fun processLoadedBankList(response: GetBankListResponse) {
        val bankList: ArrayList<Bank>? = getBankListFromResponse(response)
        if (bankList.isNullOrEmpty()) {
            bankListState.value = OnBankListLoaded(arrayListOf())
        } else {
            masterBankList.value = bankList
            bankListState.value = OnBankListLoaded(bankList = bankList)
        }
    }

    private fun getBankListFromResponse(response: GetBankListResponse): ArrayList<Bank>? {
        return response.bankListResponse?.bankData?.bankList
    }

    private suspend fun loadBankListGQL(param: Map<String, Any>): GraphqlResponse = withContext(Dispatchers.IO) {
        val graphRequest = GraphqlRequest(rawQueries[QUERY_GET_BANK_LIST],
                GetBankListResponse::class.java, param)
        graphqlRepository.getReseponse(listOf(graphRequest))
    }

    fun searchBankByQuery(query: String) {
        cancelOldSearchJob()
        createNewSearchJob()
        launchCatchError(block = {
            val data = withContext(Dispatchers.IO + searchBankJob) {
                delay(DELAY_IN_SEARCH)
                return@withContext searchInMasterList(query)
            }
            bankListState.value = OnBankSearchResult(data)
        }) {
            it.printStackTrace()
        }
    }

    private fun cancelOldSearchJob() {
        if (searchBankJob.isActive)
            searchBankJob.cancel()
    }

    private fun createNewSearchJob() {
        searchBankJob = Job()
    }

    fun resetSearchResult() {
        cancelOldSearchJob()
        createNewSearchJob()
        launchCatchError(block = {
            val data = withContext(Dispatchers.IO + searchBankJob) {
                return@withContext masterBankList.value
            }
            data?.let {
                bankListState.value = OnBankSearchResult(data)
            } ?: run { bankListState.value = OnBankListLoadingError("No Bank found") }
        }) {
            it.printStackTrace()
        }
    }

    private fun searchInMasterList(query: String): ArrayList<Bank> {
        val searchedBankList = arrayListOf<Bank>()
        val tempBankList = masterBankList.value
        tempBankList?.forEach { bank ->
            bank.bankName?.let {
                if (bank.bankName.contains(query, true)) {
                    searchedBankList.add(bank)
                }
            }
        }
        return searchedBankList
    }

    private fun getBankListParams() = mapOf(PARAM_CURRENT_PAGE to currentPage,
            PARAM_ITEM_PER_PAGE to maxItemPerPage)

    override fun onCleared() {
        cancelOldSearchJob()
        super.onCleared()
    }

}