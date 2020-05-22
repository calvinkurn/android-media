package com.tokopedia.topads.edit.view.model

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.topads.common.data.internal.ParamObject.GROUP_ID
import com.tokopedia.topads.common.data.internal.ParamObject.PRODUCT_IDS
import com.tokopedia.topads.common.data.internal.ParamObject.SHOP_id
import com.tokopedia.topads.common.data.internal.ParamObject.TYPE
import com.tokopedia.topads.common.data.response.KeywordSuggestionResponse
import com.tokopedia.topads.edit.R
import com.tokopedia.topads.edit.view.adapter.keyword.viewmodel.KeywordItemViewModel
import com.tokopedia.topads.edit.view.adapter.keyword.viewmodel.KeywordViewModel
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class KeywordAdsViewModel @Inject constructor(
        private val context: Context,
        @Named("Main")
        private val dispatcher: CoroutineDispatcher,
        private val userSession: UserSessionInterface,
        private val repository: GraphqlRepository) : BaseViewModel(dispatcher) {


    private val selectedKeywordList = MutableLiveData<KeywordViewModel>()
    private val keywordList = HashSet<String>()
    private val searchCount = HashMap<String, ArrayList<Int>>()
    private val map = ArrayList<Int>()


    fun getSuggestionKeyword(productIds: String?, groupId: Int?, onSuccess: ((List<KeywordSuggestionResponse.Result.TopAdsGetKeywordSuggestionV3.DataItem>) -> Unit),
                             onError: ((Throwable) -> Unit)) {
        launchCatchError(
                block = {
                    val data = withContext(Dispatchers.IO) {
                        val request = GraphqlRequest(GraphqlHelper.loadRawString(context.resources, R.raw.query_ad_keyword_suggestion),
                                KeywordSuggestionResponse.Result::class.java, mapOf(PRODUCT_IDS to productIds, GROUP_ID to groupId, SHOP_id to Integer.parseInt(userSession.shopId), TYPE to 1))
                        val cacheStrategy = GraphqlCacheStrategy
                                .Builder(CacheType.CLOUD_THEN_CACHE).build()
                        repository.getReseponse(listOf(request), cacheStrategy)
                    }
                    data.getSuccessData<KeywordSuggestionResponse.Result>().topAdsGetKeywordSuggestionV3.let {
                        onSuccess(it.data)
                        updateList(it.data)
                    }
                },
                onError = {
                    onError(it)
                }
        )
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
            KeywordItemViewModel(KeywordSuggestionResponse.Result.TopAdsGetKeywordSuggestionV3.DataItem.KeywordDataItem(searchCount[keyword]?.get(0)!!, searchCount[keyword]?.get(1)!!.toString(), keyword))
        } else
            KeywordItemViewModel(KeywordSuggestionResponse.Result.TopAdsGetKeywordSuggestionV3.DataItem.KeywordDataItem(minSuggestedBid, "Tidak diketahui", keyword))
        selectedKeywordList.postValue(item)
        return item
    }

}