package com.tokopedia.graphql.data.source.cloud;

import com.google.gson.JsonArray;
import com.tokopedia.CommonUtils;
import com.tokopedia.graphql.FingerprintManager;
import com.tokopedia.graphql.GraphqlCacheManager;
import com.tokopedia.graphql.data.GqlClient;
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponseInternal;
import com.tokopedia.graphql.data.source.GraphqlDataStore;
import com.tokopedia.graphql.data.source.cloud.api.GraphqlApi;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

import static com.tokopedia.graphql.GraphqlConstant.LOG_TAG;

/**
 * Retrive the response from Cloud and dump the same in disk if cache was enable by consumer.
 */
public class GraphqlCloudDataStore implements GraphqlDataStore {
    private GraphqlApi mApi;
    private GraphqlCacheManager mCacheManager;
    private FingerprintManager mFingerprintManager;

    @Inject
    public GraphqlCloudDataStore() {
        this.mApi = GqlClient.getApiInterface();
        this.mCacheManager = new GraphqlCacheManager();
        this.mFingerprintManager = GqlClient.getFingerPrintManager();
    }

    @Override
    public Observable<GraphqlResponseInternal> getResponse(List<GraphqlRequest> requests, GraphqlCacheStrategy cacheStrategy) {
        return mApi.getResponse(requests).map(new Func1<JsonArray, GraphqlResponseInternal>() {
            @Override
            public GraphqlResponseInternal call(JsonArray jsonElements) {
                return new GraphqlResponseInternal(jsonElements, false);
            }
        }).doOnNext(new Action1<GraphqlResponseInternal>() {
            @Override
            public void call(GraphqlResponseInternal graphqlResponseInternal) {
                if (cacheStrategy != null) {
                    //trying to store the data into cache based on cache strategy;
                    switch (cacheStrategy.getType()) {
                        case NONE:
                        case CACHE_ONLY:
                            //do nothing for now
                            CommonUtils.dumper(LOG_TAG + "Not saving the data to cache.");
                            break;
                        case CACHE_FIRST:
                        case ALWAYS_CLOUD:
                            //store the data into disk
                            CommonUtils.dumper(LOG_TAG + "Saving the data to cache.");
                            mCacheManager.save(mFingerprintManager.generateFingerPrint(requests.toString(), cacheStrategy.isSessionIncluded()),
                                    graphqlResponseInternal.getOriginalResponse().toString(),
                                    cacheStrategy.getExpiryTime());
                    }
                }
            }
        });
    }
}
