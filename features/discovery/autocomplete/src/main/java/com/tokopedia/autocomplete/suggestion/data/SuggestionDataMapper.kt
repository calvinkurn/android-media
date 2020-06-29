package com.tokopedia.autocomplete.suggestion.data

import com.tokopedia.graphql.data.model.GraphqlResponse
import rx.functions.Func1

class SuggestionDataMapper : Func1<GraphqlResponse, SuggestionUniverse> {
    override fun call(graphqlResponse: GraphqlResponse?): SuggestionUniverse? {
        return graphqlResponse?.getData<SuggestionResponse>(SuggestionResponse::class.java)?.suggestionUniverse
    }
}