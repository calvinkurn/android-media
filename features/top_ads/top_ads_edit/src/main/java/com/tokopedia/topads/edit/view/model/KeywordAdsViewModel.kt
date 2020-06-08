package com.tokopedia.topads.edit.view.model

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.topads.common.data.response.KeywordSuggestionResponse
import com.tokopedia.topads.edit.usecase.SuggestionKeywordUseCase
import com.tokopedia.topads.edit.view.adapter.keyword.viewmodel.KeywordItemViewModel
import com.tokopedia.topads.edit.view.adapter.keyword.viewmodel.KeywordViewModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class KeywordAdsViewModel @Inject constructor(
        private val dispatcher: CoroutineDispatcher,
        private val suggestionKeywordUseCase: SuggestionKeywordUseCase) : BaseViewModel(dispatcher) {


    private val selectedKeywordList = MutableLiveData<KeywordViewModel>()
    private val keywordList = HashSet<String>()
    private val searchCount = HashMap<String, ArrayList<Int>>()
    private val map = ArrayList<Int>()


    fun getSuggestionKeyword(productIds: String?, groupId: Int?, onSuccess: ((List<KeywordSuggestionResponse.Result.TopAdsGetKeywordSuggestionV3.DataItem>) -> Unit)) {

        suggestionKeywordUseCase.setParams(groupId, productIds?.trim())
        suggestionKeywordUseCase.executeQuerySafeMode({
            onSuccess(it.topAdsGetKeywordSuggestionV3.data)
            updateList(it.topAdsGetKeywordSuggestionV3.data)
        }, { throwable ->
            throwable.printStackTrace()
        })
    }

    private fun updateList(data: List<KeywordSuggestionResponse.Result.TopAdsGetKeywordSuggestionV3.DataItem>) {
        keywordList.clear()

        data.forEach { dataItem ->
            dataItem.keywordData.forEach {
                keywordList.add(KeywordItemViewModel(it).data.keyword)
                map.add(KeywordItemViewModel(it).data.bidSuggest)
                map.add(Integer.parseInt(KeywordItemViewModel(it).data.totalSearch))
                searchCount[KeywordItemViewModel(it).data.keyword] = map

            }
        }
    }

    fun addNewKeyword(keyword: String, minSuggestedBid: Int): KeywordItemViewModel {
        val item: KeywordItemViewModel = if (keywordList.contains(keyword) && searchCount.containsKey(keyword)) {
            KeywordItemViewModel(KeywordSuggestionResponse.Result.TopAdsGetKeywordSuggestionV3.DataItem.KeywordDataItem(searchCount[keyword]?.get(0)!!, searchCount[keyword]?.get(1)!!.toString(), keyword, "es"))
        } else
            KeywordItemViewModel(KeywordSuggestionResponse.Result.TopAdsGetKeywordSuggestionV3.DataItem.KeywordDataItem(minSuggestedBid, "Tidak diketahui", keyword, "es"))
        selectedKeywordList.postValue(item)
        return item
    }
    override fun onCleared() {
        super.onCleared()
        suggestionKeywordUseCase.cancelJobs()
    }

}