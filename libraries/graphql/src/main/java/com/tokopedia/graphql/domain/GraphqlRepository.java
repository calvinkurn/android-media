package com.tokopedia.graphql.domain;

import com.tokopedia.graphql.data.model.GraphqlCacheStrategy;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponseInternal;

import java.util.List;

import rx.Observable;

/**
 * Data repository interface, It should be implemented by data layer
 */
public interface GraphqlRepository {
    Observable<GraphqlResponseInternal> getResponse(List<GraphqlRequest> requests, GraphqlCacheStrategy cacheStrategy);
}
