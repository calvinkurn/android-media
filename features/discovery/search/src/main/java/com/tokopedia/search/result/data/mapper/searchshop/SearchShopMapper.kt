package com.tokopedia.search.result.data.mapper.searchshop

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.search.result.domain.model.SearchShopModelKt

import rx.functions.Func1

internal class SearchShopMapper : Func1<GraphqlResponse, SearchShopModelKt> {

    override fun call(response: GraphqlResponse?): SearchShopModelKt? {
        return response?.getData<SearchShopModelKt>(GraphqlResponse::class.java)
    }
}
