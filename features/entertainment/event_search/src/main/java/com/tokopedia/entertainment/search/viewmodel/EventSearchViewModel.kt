package com.tokopedia.entertainment.search.viewmodel

import android.content.res.Resources
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.entertainment.search.R
import com.tokopedia.entertainment.search.adapter.SearchEventItem
import com.tokopedia.entertainment.search.adapter.viewholder.HistoryBackgroundItemViewHolder
import com.tokopedia.entertainment.search.adapter.viewholder.SearchEventListViewHolder
import com.tokopedia.entertainment.search.adapter.viewholder.SearchLocationListViewHolder
import com.tokopedia.entertainment.search.adapter.viewmodel.FirstTimeViewModel
import com.tokopedia.entertainment.search.adapter.viewmodel.HistoryViewModel
import com.tokopedia.entertainment.search.adapter.viewmodel.SearchEventViewModel
import com.tokopedia.entertainment.search.adapter.viewmodel.SearchLocationViewModel
import com.tokopedia.entertainment.search.data.EventSearchFullLocationResponse
import com.tokopedia.entertainment.search.data.EventSearchHistoryResponse
import com.tokopedia.entertainment.search.data.EventSearchLocationResponse
import com.tokopedia.entertainment.search.data.mapper.SearchMapper
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

/**
 * Author errysuprayogi on 04,March,2020
 */

class EventSearchViewModel(private val dispatcher: CoroutineDispatcher,
                           private val gqlRepository: GraphqlRepository,
                           private val userSession: UserSessionInterface) : BaseViewModel(dispatcher){
    companion object{
        private val TAG = EventSearchViewModel::class.java.simpleName
        private val SEARCHQUERY = "search_query"
    }

    val searchList: MutableLiveData<List<SearchEventItem<*>>> by lazy { MutableLiveData<List<SearchEventItem<*>>>() }
    val listViewHolder : MutableList<SearchEventItem<*>> = mutableListOf()
    val isItRefreshing : MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    val errorReport : MutableLiveData<String> by lazy { MutableLiveData<String>() }

    fun getHistorySearch(resources: Resources){
        launchCatchError(
                block = {
                    val lists: MutableList<HistoryBackgroundItemViewHolder.EventModel> = mutableListOf()
                    listViewHolder.clear()
                    if(userSession.isLoggedIn){
                        val data = getHistorySearchData(resources)
                        data.let {
                            it.travelCollectiveRecentSearches.let {
                                it.items.forEach {
                                    lists.add(SearchMapper.mappingRecentSearch(it))
                                }
                                listViewHolder.add(HistoryViewModel(lists))

                                searchList.value = listViewHolder
                                isItRefreshing.value = false
                            }
                        }
                    }else{
                        listViewHolder.add(FirstTimeViewModel())
                        searchList.value = listViewHolder
                        isItRefreshing.value = false
                    }
                },
                onError = {
                    errorReport.value = it.message
                    isItRefreshing.value = false
                }
        )
    }

    fun getSearchData(resources: Resources, text: String){
        Timber.tag(TAG + " before clear").d(listViewHolder.size.toString())
        launchCatchError(
                block = {
                    val listsLocation : MutableList<SearchLocationListViewHolder.LocationSuggestion> = mutableListOf()
                    val listsKegiatan : MutableList<SearchEventListViewHolder.KegiatanSuggestion> = mutableListOf()
                    val dataLocation = getLocationSuggestionData(resources, text)
                    listViewHolder.clear()
                    Timber.tag(TAG + " after clear").d(listViewHolder.size.toString())
                    dataLocation.let {
                        it.eventLocationSearch.let {
                            if(it.count.toInt()  > 0){
                                it.locations.forEach {
                                    listsLocation.add(SearchMapper.mappingLocationSuggestion(it))
                                }
                                listViewHolder.add(SearchLocationViewModel(listsLocation, query = text))
                            }
                        }
                        it.eventSearch.let {
                            if(it.count.toInt() > 0){
                                it.products.forEach{
                                    if(!it.childCategoryIds.equals("3")){
                                        listsKegiatan.add(SearchMapper.mappingEventSuggestion(it))
                                    }
                                }
                                listViewHolder.add(SearchEventViewModel(listsKegiatan, resources))
                            }
                        }
                        searchList.value = listViewHolder
                        isItRefreshing.value = false
                    }
                },
                onError = {
                    errorReport.value = it.message
                    isItRefreshing.value = false
                }
        )
    }

    private suspend fun getLocationSuggestionData(resources: Resources, text: String) : EventSearchLocationResponse.Data{
        return withContext(Dispatchers.IO){
            val req = GraphqlRequest(
                    GraphqlHelper.loadRawString(resources, R.raw.query_event_search_location),
                    EventSearchLocationResponse.Data::class.java, mapOf(SEARCHQUERY to text)
            )
            val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
            gqlRepository.getReseponse(listOf(req), cacheStrategy).getSuccessData<EventSearchLocationResponse.Data>()
        }
    }

    private suspend fun getHistorySearchData(resources : Resources): EventSearchHistoryResponse.Data{
        return withContext(Dispatchers.IO){
            val req = GraphqlRequest(
                    GraphqlHelper.loadRawString(resources, R.raw.query_event_search_history),
                    EventSearchHistoryResponse.Data::class.java)
            val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST).build()
            gqlRepository.getReseponse(listOf(req), cacheStrategy).getSuccessData<EventSearchHistoryResponse.Data>()
        }
    }


}