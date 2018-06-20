package com.tokopedia.networklib.data.source;

import com.tokopedia.networklib.data.model.RestRequest;
import com.tokopedia.networklib.data.model.RestResponseIntermediate;

import rx.Observable;

public interface RestDataStore {
    Observable<RestResponseIntermediate> getResponse(RestRequest requests);
}
