package com.tokopedia.graphql.data.source.cloud;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.JsonElement;
import com.akamai.botman.CYFMonitor;
import com.google.gson.JsonArray;
import com.tokopedia.graphql.FingerprintManager;
import com.tokopedia.graphql.GraphqlCacheManager;
import com.tokopedia.graphql.GraphqlConstant;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.graphql.data.model.BackendCache;
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponseInternal;
import com.tokopedia.graphql.data.source.GraphqlDataStore;
import com.tokopedia.graphql.data.source.cloud.api.GraphqlApi;
import com.tokopedia.graphql.util.CacheHelper;

import java.io.InterruptedIOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import okhttp3.internal.http2.ConnectionShutdownException;
import rx.Observable;
import timber.log.Timber;

import static com.tokopedia.akamai_bot_lib.UtilsKt.isAkamai;
import static com.tokopedia.graphql.util.Const.AKAMAI_SENSOR_DATA_HEADER;

/**
 * Retrive the response from Cloud and dump the same in disk if cache was enable by consumer.
 */
public class GraphqlCloudDataStore implements GraphqlDataStore {
    private GraphqlApi mApi;
    private GraphqlCacheManager mCacheManager;
    private FingerprintManager mFingerprintManager;
    private static String TAG = GraphqlCloudDataStore.class.getSimpleName();

    @Inject
    public GraphqlCloudDataStore() {
        this.mApi = GraphqlClient.getApiInterface();
        this.mCacheManager = new GraphqlCacheManager();
        this.mFingerprintManager = GraphqlClient.getFingerPrintManager();
    }

    /*
    * akamai wrapper for generating a sensor data
    * the hash will be passing into header of
    * X-acf-sensor-data;
    * */
    private Observable<JsonArray> getResponse(List<GraphqlRequest> requests) {
        CYFMonitor.setLogLevel(CYFMonitor.INFO);
        if (isAkamai(requests.get(0).getQuery())) {
            Map<String, String> header = new HashMap<>();
            header.put(AKAMAI_SENSOR_DATA_HEADER, GraphqlClient.getFunction().getAkamaiValue());
            return mApi.getResponse(requests, header);
        } else {
            return mApi.getResponse(requests, new HashMap<>());
        }
    }

    @Override
    public Observable<GraphqlResponseInternal> getResponse(List<GraphqlRequest> requests, GraphqlCacheStrategy cacheStrategy) {
        if (requests == null || requests.isEmpty()) {
            Log.d(TAG, "No request available for network-call, hence stopping network communication.");
            return Observable.just(new GraphqlResponseInternal(null, false));
        }

        return mApi.getResponse(requests, FingerprintManager.getQueryDigest(requests))
                .doOnError(throwable -> {
                    if (!(throwable instanceof UnknownHostException) &&
                            !(throwable instanceof SocketException) &&
                            !(throwable instanceof InterruptedIOException) &&
                            !(throwable instanceof ConnectionShutdownException)) {
                        Timber.e(throwable, "P1#REQUEST_ERROR_GQL#%s", requests.toString());
                    }
                }).map(httpResponse -> {
                    if (httpResponse == null || httpResponse.code() != 200) {
                        return null; //TODO lavekush-t need to handle and discuss the scenario with team.
                    }

                    return new GraphqlResponseInternal(httpResponse.body(), false, httpResponse.headers().get(GraphqlConstant.GqlApiKeys.CACHE));

                }).doOnNext(graphqlResponseInternal -> {
                    //Handling backend cache
                    Map<String, BackendCache> caches = CacheHelper.parseCacheHeaders(graphqlResponseInternal.getBeCache());
                    if (caches == null || caches.isEmpty()) {
                        return;
                    }

                    int size = requests.size();
                    for (int i = 0; i < size; i++) {

                        if (requests.get(i) == null || requests.get(i).isNoCache() || caches.get(requests.get(i).getMd5()) == null) {
                            continue;
                        }

                        //TODO need to save response of individual query
                        BackendCache cache = caches.get(requests.get(i).getMd5());
                        JsonElement object = graphqlResponseInternal.getOriginalResponse().get(i).getAsJsonObject().get(GraphqlConstant.GqlApiKeys.DATA);
                        if (object != null && cache != null) {
                            mCacheManager.save(requests.get(i).cacheKey(), object.toString(), cache.getMaxAge() * 1000);
                        }
                    }


                    if (cacheStrategy == null || graphqlResponseInternal == null) {
                        return;
                    }

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

                });
    }

    public GraphqlCacheManager getCacheManager() {
        return mCacheManager;
    }

    public FingerprintManager getFingerprintManager() {
        return mFingerprintManager;
    }
}
