package com.tokopedia.entertainment.search.viewmodel

import android.content.res.Resources
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.entertainment.search.adapter.SearchEventItem
import com.tokopedia.entertainment.search.adapter.viewmodel.*
import com.tokopedia.entertainment.search.data.EventSearchHistoryResponse
import com.tokopedia.entertainment.search.data.EventSearchLocationResponse
import com.tokopedia.entertainment.search.data.mapper.SearchMapper
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.*
import javax.inject.Inject

/**
 * Author errysuprayogi on 04,March,2020
 */

class EventSearchViewModel @Inject constructor(private val dispatcher: CoroutineDispatchers,
                           private val gqlRepository: GraphqlRepository) : BaseViewModel(dispatcher.main){
    companion object{
        private val TAG = EventSearchViewModel::class.java.simpleName
        private val SEARCHQUERY = "search_query"
    }

    lateinit var resources: Resources
    lateinit var job: Job

    val searchList: MutableLiveData<List<SearchEventItem<*>>> by lazy { MutableLiveData<List<SearchEventItem<*>>>() }
    val listViewHolder : MutableList<SearchEventItem<*>> = mutableListOf()
    val isItRefreshing = MutableLiveData<Boolean>()

    val errorReport : MutableLiveData<Throwable> by lazy { MutableLiveData<Throwable>() }

    fun getHistorySearch(cacheType: CacheType, query: String, isUserLogin: Boolean){
        launchCatchError(
                block = {
                    listViewHolder.clear()
                    if(isUserLogin){
                        val data = getHistorySearchData(cacheType,query)
                        data.let {
                                searchList.value = SearchMapper.mappingHistorytoSearchList(data)
                                isItRefreshing.value = false
                        }
                    }else{
                        listViewHolder.add(FirstTimeModel())
                        searchList.value = listViewHolder
                        isItRefreshing.value = false
                    }
                },
                onError = {
                    withContext(dispatcher.io) {
                        errorReport.postValue(it)
                        isItRefreshing.postValue(false)
                    }
                }
        )
    }

    fun getSearchData(text: String, cacheType: CacheType, query: String){
        job = launchCatchError(
                block = {
                    val dataLocation = getLocationSuggestionData(text, cacheType,query)
                    dataLocation.let {
                        searchList.value = SearchMapper.mappingLocationandKegiatantoSearchList(it,text,resources)
                        isItRefreshing.value = false
                    }
                },
                onError = {
                    withContext(dispatcher.io) {
                        errorReport.postValue(it)
                        isItRefreshing.postValue(false)
                    }
                }
        )
    }

    fun cancelRequest(){
        if(::job.isInitialized) job.cancel()
    }

    suspend fun getLocationSuggestionData(text: String, cacheType: CacheType, query: String) : EventSearchLocationResponse.Data{
        return withContext(dispatcher.io){
            val req = GraphqlRequest(
                    query,
                    EventSearchLocationResponse.Data::class.java, mapOf(SEARCHQUERY to text)
            )
            val cacheStrategy = GraphqlCacheStrategy.Builder(cacheType).build()
            gqlRepository.getReseponse(listOf(req), cacheStrategy).getSuccessData<EventSearchLocationResponse.Data>()
        }
    }

    suspend fun getHistorySearchData(cacheType: CacheType, query:String): EventSearchHistoryResponse.Data{
        return withContext(dispatcher.io){
            val req = GraphqlRequest(
                    query,
                    EventSearchHistoryResponse.Data::class.java)
            val cacheStrategy = GraphqlCacheStrategy.Builder(cacheType).build()
            gqlRepository.getReseponse(listOf(req), cacheStrategy).getSuccessData<EventSearchHistoryResponse.Data>()
        }
    }


}