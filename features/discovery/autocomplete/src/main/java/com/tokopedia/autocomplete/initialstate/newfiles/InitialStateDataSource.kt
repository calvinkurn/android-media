package com.tokopedia.autocomplete.initialstate.newfiles

import com.tokopedia.autocomplete.initialstate.popularsearch.PopularSearchResponse
import com.tokopedia.autocomplete.initialstate.popularsearch.PopularSearchResponseMapper
import com.tokopedia.autocomplete.network.AutocompleteCache
import com.tokopedia.cachemanager.CacheManager
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import rx.Observable
import java.util.concurrent.TimeUnit

class InitialStateDataSource(
        private val initialStateApi: InitialStateApi,
        private val initialStateMapper: InitialStateMapper,
        private val popularSearchResponseMapper: PopularSearchResponseMapper,
        private val cacheManager: CacheManager
) {
    fun getInitialState(param: HashMap<String, Any>): Observable<List<InitialStateData>> {
        return initialStateApi.getInitialState(param)
                .debounce(300, TimeUnit.MILLISECONDS)
                .doOnNext { response ->
                    val tenMinute = 600000
                    cacheManager.put(AutocompleteCache.Key.UNIVERSEARCH, response.body(), tenMinute.toLong())
                }
                .map(initialStateMapper)
    }

    fun deleteRecentSearch(parameters: HashMap<String, Any>): Observable<Response<Void>> {
        return initialStateApi.deleteRecentSearch(parameters)
    }

    fun refreshPopularSearch(parameters: HashMap<String, Any>): Observable<List<InitialStateItem>> {
        return initialStateApi.refreshPopularSearch(parameters)
                .debounce(300, TimeUnit.MILLISECONDS)
                .map(popularSearchResponseMapper)
    }

    fun getInitialStateFlow(param: HashMap<String, Any>): InitialStateResponse {
        return initialStateApi.getInitialStateFlow(param)
    }
}