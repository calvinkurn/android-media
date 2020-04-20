package com.tokopedia.autocomplete.initialstate.data

import com.tokopedia.autocomplete.initialstate.InitialStateData
import com.tokopedia.autocomplete.initialstate.InitialStateMapper
import com.tokopedia.autocomplete.network.AutocompleteCache
import com.tokopedia.cachemanager.CacheManager
import rx.Observable
import java.util.concurrent.TimeUnit

class InitialStateDataSource(
        private val initialStateApi: InitialStateApi,
        private val initialStateMapper: InitialStateMapper,
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

    fun deleteRecentSearch(parameters: HashMap<String, Any>): Observable<Boolean> {
        return initialStateApi.deleteRecentSearch(parameters).map {
            it.isSuccessful
        }
    }
}