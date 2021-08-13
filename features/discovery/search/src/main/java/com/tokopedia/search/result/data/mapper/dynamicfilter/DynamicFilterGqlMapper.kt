package com.tokopedia.search.result.data.mapper.dynamicfilter

import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.search.result.data.response.GqlDynamicFilterResponse
import com.tokopedia.search.result.domain.model.AceSearchProductModel
import com.tokopedia.search.result.domain.model.SearchProductModel
import rx.functions.Func1

internal class DynamicFilterGqlMapper : Func1<GraphqlResponse?, DynamicFilterModel?> {
    override fun call(graphqlResponse: GraphqlResponse?): DynamicFilterModel? {
        if (graphqlResponse == null) return DynamicFilterModel()

        val gqlDynamicFilterResponse = graphqlResponse.getData<GqlDynamicFilterResponse>(GqlDynamicFilterResponse::class.java)
                ?: return DynamicFilterModel()
        
        return gqlDynamicFilterResponse.dynamicFilterModel
    }
}