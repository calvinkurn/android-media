package com.tokopedia.autocompletecomponent.suggestion.domain

import com.tokopedia.autocompletecomponent.suggestion.domain.model.SuggestionUniverse
import com.tokopedia.autocompletecomponent.test.R
import com.tokopedia.autocompletecomponent.utils.rawToObject
import com.tokopedia.usecase.coroutines.UseCase
import kotlinx.coroutines.Dispatchers

class SuggestionUseCaseTest: UseCase<SuggestionUniverse>(
    Dispatchers.Unconfined,
    Dispatchers.Unconfined,
) {

    override suspend fun executeOnBackground(): SuggestionUniverse =
        rawToObject<SuggestionTestResponse>(R.raw.suggestion_response_1)
            .suggestionUniverse
}