package com.tokopedia.autocomplete.suggestion.data

import com.tokopedia.autocomplete.network.AutocompleteCache
import com.tokopedia.autocomplete.suggestion.SuggestionData
import com.tokopedia.cachemanager.CacheManager
import rx.Observable
import java.util.concurrent.TimeUnit

class SuggestionDataSource(
        private val suggestionApi: SuggestionApi,
        private val suggestionMapper: SuggestionMapper,
        private val cacheManager: CacheManager
) {
    fun getSuggestionResponse(param: HashMap<String, Any>): Observable<SuggestionData> {
        return suggestionApi.getSuggestionResponse(param)
                .debounce(300, TimeUnit.MILLISECONDS)
                .doOnNext { response ->
                    val tenMinute = 600000
                    cacheManager.put(AutocompleteCache.Key.UNIVERSEARCH, response.body(), tenMinute.toLong())
                }
                .map(suggestionMapper)
    }

    fun hitSuggestionUrlTracker(url: String): Observable<Void?> {
        return suggestionApi.hitSuggestionUrlTracker(url)
                .map { response -> response?.body() }
    }
}