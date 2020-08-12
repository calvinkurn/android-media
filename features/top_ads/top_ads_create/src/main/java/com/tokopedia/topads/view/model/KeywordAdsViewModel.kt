package com.tokopedia.topads.view.model

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
import com.tokopedia.topads.create.R
import com.tokopedia.topads.data.response.ResponseKeywordSuggestion
import com.tokopedia.topads.view.adapter.keyword.viewmodel.KeywordItemViewModel
import com.tokopedia.topads.view.adapter.keyword.viewmodel.KeywordViewModel
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

/**
 * Author errysuprayogi on 06,November,2019
 */
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


    fun getSugestionKeyword(productIds: String, groupId: Int, onSuccess: ((List<ResponseKeywordSuggestion.KeywordData>) -> Unit),
                            onError: ((Throwable) -> Unit), onEmpty: (() -> Unit)) {
        launchCatchError(
                block = {
                    val data = withContext(Dispatchers.IO) {
                        val request = GraphqlRequest(GraphqlHelper.loadRawString(context.resources, R.raw.query_ad_keyword_suggestion),
                                ResponseKeywordSuggestion.Result::class.java, mapOf(PRODUCT_IDS to productIds, GROUP_ID to groupId, SHOP_id to Integer.parseInt(userSession.shopId)))
                        val cacheStrategy = GraphqlCacheStrategy
                                .Builder(CacheType.CLOUD_THEN_CACHE).build()
                        repository.getReseponse(listOf(request), cacheStrategy)
                    }
                    data.getSuccessData<ResponseKeywordSuggestion.Result>().let {
                        if (it.topAdsGetKeywordSuggestionV3.data.isEmpty()) {
                            onEmpty()
                        } else {
                            onSuccess(it.topAdsGetKeywordSuggestionV3.data)
                            updateList(it.topAdsGetKeywordSuggestionV3.data)
                        }
                    }
                },
                onError = {
                    onError(it)
                }
        )
    }

    private fun updateList(data: List<ResponseKeywordSuggestion.KeywordData>) {
        keywordList.clear()
        data.forEach { dataItem ->
            dataItem.keywordData.forEach { index ->
                keywordList.add(KeywordItemViewModel(index).data.keyword)
                map.add(KeywordItemViewModel(index).data.bidSuggest)
                map.add(Integer.parseInt(KeywordItemViewModel(index).data.totalSearch))
                searchCount[KeywordItemViewModel(index).data.keyword] = map
            }
        }
    }

    fun addNewKeyword(keyword: String): KeywordItemViewModel {
        val item: KeywordItemViewModel = if (keywordList.contains(keyword) && searchCount.containsKey(keyword)) {
            KeywordItemViewModel(ResponseKeywordSuggestion.KeywordDataItem(searchCount[keyword]?.get(0)!!, searchCount[keyword]?.get(1)!!.toString(), keyword, "es"))
        } else
            KeywordItemViewModel(ResponseKeywordSuggestion.KeywordDataItem(0, "Tidak diketahui", keyword, "es"))
        selectedKeywordList.postValue(item)
        return item
    }

}