package com.tokopedia.search.result.data.mapper.initiatesearch;

import com.tokopedia.discovery.newdiscovery.domain.model.InitiateSearchModel;
import com.tokopedia.graphql.data.model.GraphqlResponse;

import rx.functions.Func1;

final class InitiateSearchMapper implements Func1<GraphqlResponse, InitiateSearchModel> {

    InitiateSearchMapper() {

    }

    @Override
    public InitiateSearchModel call(GraphqlResponse graphqlResponse) {
        if(graphqlResponse == null) return null;

        return graphqlResponse.getData(InitiateSearchModel.class);
    }
}
