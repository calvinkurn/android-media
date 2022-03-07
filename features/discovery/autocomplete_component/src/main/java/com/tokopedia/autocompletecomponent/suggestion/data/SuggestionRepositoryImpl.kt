package com.tokopedia.autocompletecomponent.suggestion.data

import com.tokopedia.autocompletecomponent.suggestion.domain.SuggestionRepository
import rx.Observable

class SuggestionRepositoryImpl(private val suggestionDataSource: SuggestionDataSource) : SuggestionRepository {
    override fun hitSuggestionUrlTracker(url: String): Observable<Void?> {
        return suggestionDataSource.hitSuggestionUrlTracker(url)
    }
}