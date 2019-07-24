package com.tokopedia.graphql.data.source.cloud;

import android.text.TextUtils;

import com.tokopedia.graphql.FingerprintManager;
import com.tokopedia.graphql.GraphqlCacheManager;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponseInternal;
import com.tokopedia.graphql.data.source.GraphqlDataStore;
import com.tokopedia.graphql.data.source.cloud.api.GraphqlApi;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import timber.log.Timber;

/**
 * Retrive the response from Cloud and dump the same in disk if cache was enable by consumer.
 */
public class GraphqlCloudDataStore implements GraphqlDataStore {
    private GraphqlApi mApi;
    private GraphqlCacheManager mCacheManager;
    private FingerprintManager mFingerprintManager;

    @Inject
    public GraphqlCloudDataStore() {
        this.mApi = GraphqlClient.getApiInterface();
        this.mCacheManager = new GraphqlCacheManager();
        this.mFingerprintManager = GraphqlClient.getFingerPrintManager();
    }

    @Override
    public Observable<GraphqlResponseInternal> getResponse(List<GraphqlRequest> requests, GraphqlCacheStrategy cacheStrategy) {
        return mApi.getResponse(requests)
                .doOnError(throwable -> {
                    if (!(throwable instanceof UnknownHostException) && !(throwable instanceof SocketTimeoutException)) {
                        Timber.e(throwable, requests.toString());
                    }
                })
                .map(jsonElements -> new GraphqlResponseInternal(jsonElements, false)).doOnNext(graphqlResponseInternal -> {
                    if (cacheStrategy != null) {
                        //trying to store the data into cache based on cache strategy;
                        switch (cacheStrategy.getType()) {
                            case NONE:
                            case CACHE_ONLY:
                                //do nothing for now
                                break;
                            case CACHE_FIRST:
                            case ALWAYS_CLOUD:
                                //store the data into disk
                                mCacheManager.save(mFingerprintManager.generateFingerPrint(requests.toString(), cacheStrategy.isSessionIncluded()),
                                        graphqlResponseInternal.getOriginalResponse().toString(),
                                        cacheStrategy.getExpiryTime());
                                break;
                            case CLOUD_THEN_CACHE:
                                //store the data into disk if data is not empty
                                if (!TextUtils.isEmpty(graphqlResponseInternal.getOriginalResponse().toString())) {
                                    mCacheManager.save(mFingerprintManager.generateFingerPrint(requests.toString(), cacheStrategy.isSessionIncluded()),
                                            graphqlResponseInternal.getOriginalResponse().toString(),
                                            cacheStrategy.getExpiryTime());
                                }
                                break;
                        }
                    }
                });
    }
}
