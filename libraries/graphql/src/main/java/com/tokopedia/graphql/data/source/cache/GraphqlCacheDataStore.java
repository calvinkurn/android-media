package com.tokopedia.graphql.data.source.cache;

import com.google.gson.JsonParser;
import com.tokopedia.graphql.FingerprintManager;
import com.tokopedia.graphql.GraphqlCacheManager;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponseInternal;
import com.tokopedia.graphql.data.source.GraphqlDataStore;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Retrieve the response from cache only
 */
public class GraphqlCacheDataStore implements GraphqlDataStore {

    private GraphqlCacheManager mCacheManager;
    private FingerprintManager mFingerprintManager;

    @Inject
    public GraphqlCacheDataStore() {
        this.mCacheManager = new GraphqlCacheManager();
        this.mFingerprintManager = GraphqlClient.getFingerPrintManager();
    }

    @Override
    public Observable<GraphqlResponseInternal> getResponse(List<GraphqlRequest> requests, GraphqlCacheStrategy cacheStrategy) {
        try {
            String rowJson = mCacheManager.get(mFingerprintManager.generateFingerPrint(requests.toString(), cacheStrategy.isSessionIncluded()));
            return Observable.just(new GraphqlResponseInternal(new JsonParser().parse(rowJson).getAsJsonArray(), true));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Observable.just(null);
    }
}

