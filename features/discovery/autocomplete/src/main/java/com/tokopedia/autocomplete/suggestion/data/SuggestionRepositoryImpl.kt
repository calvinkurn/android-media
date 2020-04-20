package com.tokopedia.autocomplete.suggestion.data

import com.tokopedia.autocomplete.suggestion.SuggestionRepository
import rx.Observable

class SuggestionRepositoryImpl(private val suggestionDataSource: SuggestionDataSource) : SuggestionRepository {
    override fun hitSuggestionUrlTracker(url: String): Observable<Void?> {
        return suggestionDataSource.hitSuggestionUrlTracker(url)
    }
}