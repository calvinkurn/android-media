package com.tokopedia.networklib.data.source.repository;

import com.tokopedia.networklib.data.model.RestRequest;
import com.tokopedia.networklib.data.model.RestResponseInternal;
import com.tokopedia.networklib.data.source.cloud.CloudRestRestDataStore;
import com.tokopedia.networklib.domain.RestRepository;

import javax.inject.Inject;

import rx.Observable;

public class RestRepositoryImpl implements RestRepository {

    CloudRestRestDataStore mDataStore;

    @Inject
    public RestRepositoryImpl(CloudRestRestDataStore dataStore) {
        this.mDataStore = dataStore;
    }

    @Override
    public Observable<RestResponseInternal> getResponse(RestRequest requests) {
        return mDataStore.getResponse(requests);
    }
}
