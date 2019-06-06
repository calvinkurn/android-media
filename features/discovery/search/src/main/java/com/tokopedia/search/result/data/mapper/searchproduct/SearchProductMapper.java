package com.tokopedia.search.result.data.mapper.searchproduct;

import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.search.result.domain.model.SearchProductModel;

import rx.functions.Func1;

final class SearchProductMapper implements Func1<GraphqlResponse, SearchProductModel> {

    SearchProductMapper() {

    }

    @Override
    public SearchProductModel call(GraphqlResponse graphqlResponse) {
        if(graphqlResponse == null) return null;

        return graphqlResponse.getData(SearchProductModel.class);
    }
}
