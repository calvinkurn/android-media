package com.tokopedia.common.network.data.source;

import com.tokopedia.common.network.data.model.RestRequest;
import com.tokopedia.common.network.data.model.RestResponseIntermediate;

import rx.Observable;

public interface RestDataStore {
    Observable<RestResponseIntermediate> getResponse(RestRequest requests);
}
