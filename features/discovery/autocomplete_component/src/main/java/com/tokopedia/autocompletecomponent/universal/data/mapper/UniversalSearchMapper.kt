package com.tokopedia.autocompletecomponent.universal.data.mapper

import com.tokopedia.autocompletecomponent.universal.domain.model.UniversalSearchModel
import com.tokopedia.graphql.data.model.GraphqlResponse
import rx.functions.Func1

internal class UniversalSearchMapper: Func1<GraphqlResponse, UniversalSearchModel> {

    override fun call(response: GraphqlResponse?): UniversalSearchModel? {
        return response?.getData<UniversalSearchModel>(UniversalSearchModel::class.java)
    }
}