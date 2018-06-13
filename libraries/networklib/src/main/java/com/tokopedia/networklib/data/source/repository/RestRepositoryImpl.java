package com.tokopedia.networklib.data.source.repository;

import android.content.Context;

import com.tokopedia.networklib.data.model.CacheType;
import com.tokopedia.networklib.data.model.RestCacheStrategy;
import com.tokopedia.networklib.data.model.RestRequest;
import com.tokopedia.networklib.data.model.RestResponseInternal;
import com.tokopedia.networklib.data.source.cache.RestCacheDataStore;
import com.tokopedia.networklib.data.source.cloud.CloudRestRestDataStore;
import com.tokopedia.networklib.domain.RestRepository;

import javax.inject.Inject;

import okhttp3.Interceptor;
import rx.Observable;

public class RestRepositoryImpl implements RestRepository {

    private CloudRestRestDataStore mCloud;
    private RestCacheDataStore mCache;

    @Inject
    public RestRepositoryImpl() {
        this.mCloud = new CloudRestRestDataStore();
        this.mCache = new RestCacheDataStore();
    }

    public RestRepositoryImpl(Interceptor interceptor, Context context) {
        this.mCloud = new CloudRestRestDataStore(interceptor, context);
        this.mCache = new RestCacheDataStore();
    }

    @Override
    public Observable<RestResponseInternal> getResponse(RestRequest requests, RestCacheStrategy cacheStrategy) {
        if (cacheStrategy == null
                || cacheStrategy.getType() == CacheType.NONE
                || cacheStrategy.getType() == CacheType.ALWAYS_CLOUD) {
            return getCloudResponse(requests, cacheStrategy);
        } else if (cacheStrategy.getType() == CacheType.CACHE_ONLY) {
            return mCache.getResponse(requests, cacheStrategy);
        } else {
            return Observable.concat(getCachedResponse(requests, cacheStrategy), getCloudResponse(requests, cacheStrategy))
                    .first(data -> data != null);
        }
    }

    private Observable<RestResponseInternal> getCloudResponse(RestRequest requests, RestCacheStrategy cacheStrategy) {
        return mCloud.getResponse(requests, cacheStrategy);
    }

    private Observable<RestResponseInternal> getCachedResponse(RestRequest requests, RestCacheStrategy cacheStrategy) {
        if (cacheStrategy != null) {
            return mCache.getResponse(requests, cacheStrategy);
        } else {
            return mCache.getResponse(requests, new RestCacheStrategy.Builder(CacheType.NONE).build());
        }
    }
}
