package com.tokopedia.search.result.data.mapper.dynamicfilter;

import com.tokopedia.discovery.common.data.DynamicFilterModel;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.search.result.data.response.GqlDynamicFilterResponse;

import rx.functions.Func1;

final class DynamicFilterGqlMapper implements Func1<GraphqlResponse, DynamicFilterModel> {

    DynamicFilterGqlMapper() {

    }

    @Override
    public DynamicFilterModel call(GraphqlResponse graphqlResponse) {
        if(graphqlResponse == null) return null;

        GqlDynamicFilterResponse gqlDynamicFilterResponse = graphqlResponse.getData(GqlDynamicFilterResponse.class);
        if(gqlDynamicFilterResponse == null) return null;

        return gqlDynamicFilterResponse.getDynamicFilterModel();
    }
}
