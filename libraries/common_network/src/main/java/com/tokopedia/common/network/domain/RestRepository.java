package com.tokopedia.common.network.domain;

import com.tokopedia.common.network.data.model.RestRequest;
import com.tokopedia.common.network.data.model.RestResponseIntermediate;

import java.util.List;

import rx.Observable;

public interface RestRepository {
    Observable<RestResponseIntermediate> getResponse(RestRequest requests);

    Observable<List<RestResponseIntermediate>> getResponses(List<RestRequest> requests);
}
