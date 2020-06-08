package com.tokopedia.entertainment.search.viewmodel

import android.content.res.Resources
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.search.adapter.SearchEventItem
import com.tokopedia.entertainment.search.adapter.viewholder.SearchLocationListViewHolder
import com.tokopedia.entertainment.search.adapter.viewmodel.SearchLocationViewModel
import com.tokopedia.entertainment.search.data.EventSearchFullLocationResponse
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

class
EventLocationViewModel(private val dispatcher: CoroutineDispatcher,
                             private val gqlRepository: GraphqlRepository,
                             private val userSession: UserSessionInterface) : BaseViewModel(dispatcher) {

    companion object{
        private val TAG = EventSearchViewModel::class.java.simpleName
    }

    lateinit var resources: Resources
    val searchList: MutableLiveData<List<SearchEventItem<*>>> by lazy { MutableLiveData<List<SearchEventItem<*>>>() }
    val listViewHolder : MutableList<SearchEventItem<*>> = mutableListOf()

    val errorReport : MutableLiveData<String> by lazy { MutableLiveData<String>() }

    fun getFullLocationData(){
        launchCatchError(
                block = {
                    val lists: MutableList<SearchLocationListViewHolder.LocationSuggestion> = mutableListOf()
                    listViewHolder.clear()
                    val dataLocation = getFullLocationSuggestionData()
                    dataLocation.let {
                        it.eventLocationSearch.let {
                            if(it.count.toInt() > 0){
                                it.locations.forEach{
                                    lists.add(SearchMapper.mappingLocationFullSuggestion(it))
                                }
                                listViewHolder.add(SearchLocationViewModel(lists, allLocation = true))
                            }
                        }
                        searchList.value = listViewHolder
                    }
                },
                onError = {
                    errorReport.value = it.message
                }
        )
    }

    suspend fun getFullLocationSuggestionData() : EventSearchFullLocationResponse.Data{
        return withContext(Dispatchers.IO){
            val req = GraphqlRequest(
                    GraphqlHelper.loadRawString(resources, R.raw.query_event_search_location_full),
                    EventSearchFullLocationResponse.Data::class.java
            )
            val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
            gqlRepository.getReseponse(listOf(req), cacheStrategy).getSuccessData<EventSearchFullLocationResponse.Data>()
        }
    }
}