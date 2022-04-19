package com.tokopedia.autocompletecomponent.suggestion.data

import rx.Observable

class SuggestionDataSource(
        private val suggestionApi: SuggestionApi
) {
    fun hitSuggestionUrlTracker(url: String): Observable<Void?> {
        return suggestionApi.hitSuggestionUrlTracker(url)
                .map { response -> response?.body() }
    }
}