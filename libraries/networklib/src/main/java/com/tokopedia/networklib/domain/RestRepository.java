package com.tokopedia.networklib.domain;

import com.tokopedia.networklib.data.model.RestCacheStrategy;
import com.tokopedia.networklib.data.model.RestRequest;
import com.tokopedia.networklib.data.model.RestResponseInternal;

import rx.Observable;

public interface RestRepository {
    Observable<RestResponseInternal> getResponse(RestRequest requests, RestCacheStrategy cacheStrategy);
}
