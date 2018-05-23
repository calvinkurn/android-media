package com.tokopedia.commongraphql;

import com.tokopedia.usecase.RequestParams;

import rx.Observable;

/**
 * @author ricoharisin .
 */

public class BaseGraphqlRepository implements IBaseGraphqlRepository {

    private BaseGraphqlCloudDataStore baseGraphqlCloudDataStore;

    public BaseGraphqlRepository(BaseGraphqlCloudDataStore baseGraphqlCloudDataStore) {
        this.baseGraphqlCloudDataStore = baseGraphqlCloudDataStore;
    }

    @Override
    public Observable<String> request(RequestParams requestParams) {
        return baseGraphqlCloudDataStore.request(requestParams);
    }
}
