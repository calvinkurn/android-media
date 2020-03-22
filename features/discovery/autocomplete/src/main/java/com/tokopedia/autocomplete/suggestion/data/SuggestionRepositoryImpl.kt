package com.tokopedia.autocomplete.suggestion.data

import com.tokopedia.autocomplete.suggestion.SuggestionData
import com.tokopedia.autocomplete.suggestion.SuggestionRepository
import rx.Observable
import java.util.HashMap

class SuggestionRepositoryImpl(private val suggestionDataSource: SuggestionDataSource) : SuggestionRepository {
    override fun getSuggestionResponse(parameters: HashMap<String, Any>): Observable<SuggestionData> {
        return suggestionDataSource.getSuggestionResponse(parameters)
    }

    override fun hitSuggestionUrlTracker(url: String): Observable<Void?> {
        return suggestionDataSource.hitSuggestionUrlTracker(url)
    }
}