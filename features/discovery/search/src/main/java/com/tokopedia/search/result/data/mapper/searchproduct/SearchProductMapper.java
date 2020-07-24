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

        SearchProductModel data = graphqlResponse.getData(SearchProductModel.class);

        if(data == null) throw new RuntimeException(graphqlResponse.getError(SearchProductModel.class).toString());
        else return data;
    }
}
