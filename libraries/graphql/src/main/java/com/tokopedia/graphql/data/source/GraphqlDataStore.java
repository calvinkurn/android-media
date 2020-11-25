package com.tokopedia.graphql.data.source;

import com.tokopedia.graphql.data.model.GraphqlCacheStrategy;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponseInternal;

import java.util.List;

import rx.Observable;

/**
 * Use kotlin version
 */
@Deprecated
public interface GraphqlDataStore {
    Observable<GraphqlResponseInternal> getResponse(List<GraphqlRequest> requests, GraphqlCacheStrategy cacheType);
}
