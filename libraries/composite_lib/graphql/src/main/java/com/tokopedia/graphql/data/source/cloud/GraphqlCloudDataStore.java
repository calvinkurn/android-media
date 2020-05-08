package com.tokopedia.graphql.data.source.cloud;

import android.text.TextUtils;

import com.akamai.botman.CYFMonitor;
import com.google.gson.JsonArray;
import com.tokopedia.graphql.FingerprintManager;
import com.tokopedia.graphql.GraphqlCacheManager;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponseInternal;
import com.tokopedia.graphql.data.source.GraphqlDataStore;
import com.tokopedia.graphql.data.source.cloud.api.GraphqlApi;

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
        return getResponse(requests)
                .doOnError(throwable -> {
                    if (!(throwable instanceof UnknownHostException) &&
                            !(throwable instanceof SocketException) &&
                            !(throwable instanceof InterruptedIOException) &&
                            !(throwable instanceof ConnectionShutdownException)) {
                        Timber.e(throwable, "P1#REQUEST_ERROR_GQL#%s", requests.toString());
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
