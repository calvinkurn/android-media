package com.tokopedia.autocomplete.suggestion.data

import com.tokopedia.autocomplete.suggestion.SuggestionData
import com.tokopedia.graphql.data.model.GraphqlResponse
import rx.functions.Func1

class SuggestionDataMapper : Func1<GraphqlResponse, SuggestionData> {
    override fun call(graphqlResponse: GraphqlResponse?): SuggestionData? {
        return graphqlResponse?.getData<SuggestionResponse>(SuggestionResponse::class.java)?.suggestionUniverse?.data
    }
}