package com.tokopedia.entertainment.search.viewmodel

import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.entertainment.search.R
import com.tokopedia.entertainment.search.adapter.SearchEventItem
import com.tokopedia.entertainment.search.adapter.viewholder.HistoryBackgroundItemViewHolder
import com.tokopedia.entertainment.search.adapter.viewholder.SearchEventListViewHolder
import com.tokopedia.entertainment.search.adapter.viewholder.SearchLocationListViewHolder
import com.tokopedia.entertainment.search.adapter.viewmodel.*
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
import javax.annotation.Resource

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

    lateinit var resources: Resources

    val searchList: MutableLiveData<List<SearchEventItem<*>>> by lazy { MutableLiveData<List<SearchEventItem<*>>>() }
    val listViewHolder : MutableList<SearchEventItem<*>> = mutableListOf()
    val isItRefreshing = MutableLiveData<Boolean>()

    val errorReport : MutableLiveData<String> by lazy { MutableLiveData<String>() }

    fun getHistorySearch(){
        launchCatchError(
                block = {
                    val lists: MutableList<HistoryBackgroundItemViewHolder.EventModel> = mutableListOf()
                    listViewHolder.clear()
                    if(userSession.isLoggedIn){
                        val data = getHistorySearchData()
                        data.let {
                            it.travelCollectiveRecentSearches.let {
                                if(it.items.isNotEmpty()){
                                    it.items.forEach {
                                        lists.add(SearchMapper.mappingRecentSearch(it))
                                    }
                                    listViewHolder.add(HistoryViewModel(lists))
                                } else{
                                    listViewHolder.add(FirstTimeViewModel())
                                }

                                searchList.postValue(listViewHolder)
                                isItRefreshing.postValue(false)
                            }
                        }
                    }else{
                        listViewHolder.add(FirstTimeViewModel())
                        searchList.postValue(listViewHolder)
                        isItRefreshing.postValue(false)
                    }
                },
                onError = {
                    errorReport.value = it.message
                    isItRefreshing.postValue(false)
                }
        )
    }

    fun getSearchData(text: String){
        launchCatchError(
                block = {
                    val listsLocation : MutableList<SearchLocationListViewHolder.LocationSuggestion> = mutableListOf()
                    val listsKegiatan : MutableList<SearchEventListViewHolder.KegiatanSuggestion> = mutableListOf()
                    val dataLocation = getLocationSuggestionData(text)
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
                                    listsKegiatan.add(SearchMapper.mappingEventSuggestion(it))
                                }
                                if(listsKegiatan.size > 0) listViewHolder.add(SearchEventViewModel(listsKegiatan, resources))
                            }
                        }
                        if(it.eventLocationSearch.locations.isEmpty() && it.eventSearch.products.isEmpty()) {
                            listViewHolder.clear()
                            listViewHolder.add(SearchEmptyStateViewModel())
                        }
                        searchList.postValue(listViewHolder)
                        isItRefreshing.value = false
                    }
                },
                onError = {
                    it.printStackTrace()
                    errorReport.value = it.message
                    isItRefreshing.value = false
                }
        )
    }

    suspend fun getLocationSuggestionData(text: String) : EventSearchLocationResponse.Data{
        return withContext(Dispatchers.IO){
            val req = GraphqlRequest(
                    GraphqlHelper.loadRawString(resources, R.raw.query_event_search_location),
                    EventSearchLocationResponse.Data::class.java, mapOf(SEARCHQUERY to text)
            )
            val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
            gqlRepository.getReseponse(listOf(req), cacheStrategy).getSuccessData<EventSearchLocationResponse.Data>()
        }
    }

    suspend fun getHistorySearchData(): EventSearchHistoryResponse.Data{
        return withContext(Dispatchers.IO){
            val req = GraphqlRequest(
                    GraphqlHelper.loadRawString(resources, R.raw.query_event_search_history),
                    EventSearchHistoryResponse.Data::class.java)
            val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST).build()
            gqlRepository.getReseponse(listOf(req), cacheStrategy).getSuccessData<EventSearchHistoryResponse.Data>()
        }
    }


}