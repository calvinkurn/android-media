package com.tokopedia.search.result.data.mapper.searchshop

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.search.result.domain.model.SearchShopModel

import rx.functions.Func1

internal class SearchShopMapper : Func1<GraphqlResponse, SearchShopModel> {

    override fun call(response: GraphqlResponse?): SearchShopModel? {
        return response?.getData<SearchShopModel>(SearchShopModel::class.java)
    }
}
