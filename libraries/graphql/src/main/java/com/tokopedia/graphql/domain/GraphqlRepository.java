package com.tokopedia.graphql.domain;

import com.tokopedia.graphql.data.model.GraphqlCacheStrategy;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;

import java.util.List;

import rx.Observable;

/**
 * Data repository interface, It should be implemented by data layer
 * Use kotlin version
 */
@Deprecated
public interface GraphqlRepository {
    Observable<GraphqlResponse> getResponse(List<GraphqlRequest> requests, GraphqlCacheStrategy cacheStrategy);
}
