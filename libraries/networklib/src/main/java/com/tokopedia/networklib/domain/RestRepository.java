package com.tokopedia.networklib.domain;

import com.tokopedia.networklib.data.model.RestRequest;
import com.tokopedia.networklib.data.model.RestResponseIntermediate;

import java.util.List;

import rx.Observable;

public interface RestRepository {
    Observable<RestResponseIntermediate> getResponse(RestRequest requests);

    Observable<List<RestResponseIntermediate>> getResponses(List<RestRequest> requests);
}
