package com.tokopedia.autocomplete.initialstate.data

import com.tokopedia.autocomplete.initialstate.InitialStateData
import com.tokopedia.graphql.data.model.GraphqlResponse
import rx.functions.Func1

class InitialStateDataMapper : Func1<GraphqlResponse, List<InitialStateData>> {
    override fun call(graphqlResponse: GraphqlResponse?): List<InitialStateData>? {
        return graphqlResponse?.getData<InitialStateGqlResponse>(InitialStateGqlResponse::class.java)?.initialStateUniverse?.data
    }
}