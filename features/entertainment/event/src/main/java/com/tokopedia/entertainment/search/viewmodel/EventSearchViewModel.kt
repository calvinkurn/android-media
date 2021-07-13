package com.tokopedia.entertainment.search.viewmodel

import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.search.adapter.SearchEventItem
import com.tokopedia.entertainment.search.adapter.viewholder.HistoryBackgroundItemViewHolder
import com.tokopedia.entertainment.search.adapter.viewholder.SearchEventListViewHolder
import com.tokopedia.entertainment.search.adapter.viewholder.SearchLocationListViewHolder
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
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import kotlinx.coroutines.*
import timber.log.Timber
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
                                searchList.postValue(SearchMapper.mappingHistorytoSearchList(data))
                                isItRefreshing.postValue(false)
                        }
                    }else{
                        listViewHolder.add(FirstTimeModel())
                        searchList.postValue(listViewHolder)
                        isItRefreshing.postValue(false)
                    }
                },
                onError = {
                    Log.d("ERROR_SNACK", "SHOW ERROR SNACK HISTORY")
                    errorReport.postValue(it)
                    isItRefreshing.postValue(false)
                }
        )
    }

    fun getSearchData(text: String, cacheType: CacheType, query: String){
        job = launchCatchError(
                block = {
                    val dataLocation = getLocationSuggestionData(text, cacheType,query)
                    dataLocation.let {
                        searchList.postValue(SearchMapper.mappingLocationandKegiatantoSearchList(it,text,resources))
                        isItRefreshing.value = false
                    }
                },
                onError = {
                    Log.d("ERROR_SNACK", "SHOW ERROR SNACK Data")
                    errorReport.postValue(it)
                    isItRefreshing.value = false
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