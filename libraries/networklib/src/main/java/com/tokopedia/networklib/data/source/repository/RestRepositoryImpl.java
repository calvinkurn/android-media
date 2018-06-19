package com.tokopedia.networklib.data.source.repository;

import android.content.Context;

import com.tokopedia.networklib.data.model.CacheType;
import com.tokopedia.networklib.data.model.RestCacheStrategy;
import com.tokopedia.networklib.data.model.RestRequest;
import com.tokopedia.networklib.data.model.RestResponseIntermediate;
import com.tokopedia.networklib.data.source.cache.RestCacheDataStore;
import com.tokopedia.networklib.data.source.cloud.CloudRestRestDataStore;
import com.tokopedia.networklib.domain.RestRepository;

import java.util.List;

import javax.inject.Inject;

import okhttp3.Interceptor;
import rx.Observable;
import rx.functions.Func1;

public class RestRepositoryImpl implements RestRepository {

    private CloudRestRestDataStore mCloud;
    private RestCacheDataStore mCache;

    @Inject
    public RestRepositoryImpl() {
        this.mCloud = new CloudRestRestDataStore();
        this.mCache = new RestCacheDataStore();
    }

    public RestRepositoryImpl(List<Interceptor> interceptors, Context context) {
        this.mCloud = new CloudRestRestDataStore(interceptors, context);
        this.mCache = new RestCacheDataStore();
    }

    @Override
    public Observable<RestResponseIntermediate> getResponse(RestRequest request) {
        RestCacheStrategy cacheStrategy = request.getCacheStrategy();
        if (cacheStrategy == null
                || cacheStrategy.getType() == CacheType.NONE
                || cacheStrategy.getType() == CacheType.ALWAYS_CLOUD) {
            return getCloudResponse(request);
        } else if (cacheStrategy.getType() == CacheType.CACHE_ONLY) {
            return getCachedResponse(request);
        } else {
            return Observable.concat(getCachedResponse(request), getCloudResponse(request))
                    .first(data -> data != null);
        }
    }

    private Observable<RestResponseIntermediate> getCloudResponse(RestRequest requests) {
        return mCloud.getResponse(requests);
    }

    private Observable<RestResponseIntermediate> getCachedResponse(RestRequest requests) {
        return mCache.getResponse(requests);
    }

    @Override
    public Observable<List<RestResponseIntermediate>> getResponses(List<RestRequest> requests) {
        return Observable.from(requests).
                flatMap((Func1<RestRequest, Observable<RestResponseIntermediate>>) restRequest -> getResponse(restRequest)).toList();
    }
}
