package com.tokopedia.autocomplete.suggestion.data

import com.tokopedia.graphql.data.model.GraphqlResponse
import rx.functions.Func1

class SuggestionDataMapper : Func1<GraphqlResponse, SuggestionResponse> {
    override fun call(graphqlResponse: GraphqlResponse?): SuggestionResponse? {
        return graphqlResponse?.getData<SuggestionResponse>(SuggestionResponse::class.java)
    }
}