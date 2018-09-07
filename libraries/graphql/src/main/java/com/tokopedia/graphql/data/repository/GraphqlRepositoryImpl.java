package com.tokopedia.graphql.data.repository;

import com.tokopedia.graphql.data.model.CacheType;
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponseInternal;
import com.tokopedia.graphql.data.source.cache.GraphqlCacheDataStore;
import com.tokopedia.graphql.data.source.cloud.GraphqlCloudDataStore;
import com.tokopedia.graphql.domain.GraphqlRepository;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * This class will responsible for data fetching either from cloud or cache based on provided GraphqlCacheStrategy
 */
public class GraphqlRepositoryImpl implements GraphqlRepository {
    private GraphqlCloudDataStore mGraphqlCloudDataStore;
    private GraphqlCacheDataStore mGraphqlCache;

    @Inject
    public GraphqlRepositoryImpl() {
        this.mGraphqlCloudDataStore = new GraphqlCloudDataStore();
        this.mGraphqlCache = new GraphqlCacheDataStore();
    }

    @Override
    public Observable<GraphqlResponseInternal> getResponse(List<GraphqlRequest> requests, GraphqlCacheStrategy cacheStrategy) {
        if (cacheStrategy == null
                || cacheStrategy.getType() == CacheType.NONE
                || cacheStrategy.getType() == CacheType.ALWAYS_CLOUD) {
            return getCloudResponse(requests, cacheStrategy);
        } else if (cacheStrategy.getType() == CacheType.CACHE_ONLY) {
            return mGraphqlCache.getResponse(requests, cacheStrategy);
        } else {
            return Observable.concat(getCachedResponse(requests, cacheStrategy), getCloudResponse(requests, cacheStrategy))
                    .first(data -> data != null);
        }
    }

    private Observable<GraphqlResponseInternal> getCloudResponse(List<GraphqlRequest> requests, GraphqlCacheStrategy cacheStrategy) {
        return mGraphqlCloudDataStore.getResponse(requests, cacheStrategy);
    }

    private Observable<GraphqlResponseInternal> getCachedResponse(List<GraphqlRequest> requests, GraphqlCacheStrategy cacheStrategy) {
        if (cacheStrategy != null) {
            return mGraphqlCache.getResponse(requests, cacheStrategy);
        } else {
            return mGraphqlCache.getResponse(requests, new GraphqlCacheStrategy.Builder(CacheType.NONE).build());
        }
    }
}
