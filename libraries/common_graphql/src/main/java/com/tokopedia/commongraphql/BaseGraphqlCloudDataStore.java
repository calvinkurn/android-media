package com.tokopedia.commongraphql;

import com.tokopedia.usecase.RequestParams;

import rx.Observable;

/**
 * @author ricoharisin .
 */

class BaseGraphqlCloudDataStore {

    private GraphqlApi graphqlApi;

    public BaseGraphqlCloudDataStore(GraphqlApi graphqlApi) {
        this.graphqlApi = graphqlApi;
    }

    public Observable<String> request(RequestParams requestParams) {
        return graphqlApi.request(requestParams.getParamsAllValueInString());
    }
}
