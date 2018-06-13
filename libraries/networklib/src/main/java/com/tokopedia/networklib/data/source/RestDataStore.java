package com.tokopedia.networklib.data.source;

import com.tokopedia.networklib.data.model.RestCacheStrategy;
import com.tokopedia.networklib.data.model.RestRequest;
import com.tokopedia.networklib.data.model.RestResponseInternal;

import rx.Observable;

public interface RestDataStore {
    Observable<RestResponseInternal> getResponse(RestRequest requests, RestCacheStrategy cacheStrategy);
}
