package com.tokopedia.graphql.data.source.cloud;

import android.text.TextUtils;

import com.akamai.botman.CYFMonitor;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.tokopedia.graphql.CommonUtils;
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
import com.tokopedia.graphql.util.Const;
import com.tokopedia.graphql.util.LoggingUtils;
import com.tokopedia.logger.ServerLogger;
import com.tokopedia.logger.utils.Priority;

import java.io.InterruptedIOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import okhttp3.internal.http2.ConnectionShutdownException;
import retrofit2.Response;
import rx.Observable;
import timber.log.Timber;

import static com.tokopedia.akamai_bot_lib.UtilsKt.isAkamai;
import static com.tokopedia.graphql.util.Const.AKAMAI_SENSOR_DATA_HEADER;
import static com.tokopedia.graphql.util.Const.QUERY_HASHING_HEADER;

/**
 * Retrieve the response from Cloud and dump the same in disk if cache was enable by consumer.
 * Use kotlin version
 */
@Deprecated
public class GraphqlCloudDataStore implements GraphqlDataStore {
    private final GraphqlApi mApi;
    private final GraphqlCacheManager mCacheManager;
    private final FingerprintManager mFingerprintManager;
    private static final String TAG = GraphqlCloudDataStore.class.getSimpleName();

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
    private Observable<Response<JsonArray>> getResponse(List<GraphqlRequest> requests) {
        CYFMonitor.setLogLevel(CYFMonitor.INFO);
        Map<String, String> header = new HashMap<>();
        if(requests.get(0).isDoQueryHash()){
            StringBuilder queryHashingHeaderValue = new StringBuilder();
            for (GraphqlRequest graphqlRequest:requests) {
                String queryHashValue = mCacheManager.getQueryHashValue(graphqlRequest.getMd5());
                if(TextUtils.isEmpty(queryHashValue)){
                    queryHashingHeaderValue.append("");
                } else {
                    if(queryHashingHeaderValue.length() <= 0) {
                        queryHashingHeaderValue.append(queryHashValue);
                    } else {
                        queryHashingHeaderValue.append(",")
                                .append(queryHashValue);
                    }
                    graphqlRequest.setQuery("");
                }
            }
            if(TextUtils.isEmpty(queryHashingHeaderValue.toString())){
                queryHashingHeaderValue.append(";true");
            }
            else{
                queryHashingHeaderValue.append(";false");
            }
            header.put(QUERY_HASHING_HEADER, queryHashingHeaderValue.toString());
        }
        if (isAkamai(requests.get(0).getQuery())) {
            header.put(AKAMAI_SENSOR_DATA_HEADER, GraphqlClient.getFunction().getAkamaiValue());
        }
        return mApi.getResponse(requests, header, FingerprintManager.getQueryDigest(requests));
    }

    @Override
    public Observable<GraphqlResponseInternal> getResponse(List<GraphqlRequest> requests, GraphqlCacheStrategy cacheStrategy) {
        if (requests == null || requests.isEmpty()) {
            return Observable.just(new GraphqlResponseInternal(null, false));
        }

        return getResponse(requests)
                .doOnError(throwable -> {
                    if (!(throwable instanceof UnknownHostException) &&
                            !(throwable instanceof SocketException) &&
                            !(throwable instanceof InterruptedIOException) &&
                            !(throwable instanceof ConnectionShutdownException)) {
                        LoggingUtils.logGqlError("java", requests.toString(), throwable);
                    }
                }).map(httpResponse -> {
                    if (httpResponse == null) {
                        return null;
                    }
                    if(httpResponse.code() == Const.GQL_QUERY_HASHING_ERROR){
                        StringBuilder queryHashValues = new StringBuilder();
                        //Reset request bodies
                        if(requests.size() > 0) {
                            for (GraphqlRequest graphqlRequest : requests) {
                                graphqlRequest.setQuery(graphqlRequest.getQueryCopy());
                                queryHashValues.append(mCacheManager.getQueryHashValue(graphqlRequest.getMd5())).append(",");
                                mCacheManager.deleteQueryHashValue(graphqlRequest.getMd5());
                            }
                        }
                        Map<String, String> header = new HashMap<>();
                        if(requests.get(0).getQueryHashRetryCount() > 0) {
                            header.put(QUERY_HASHING_HEADER, ";true");
                            requests.get(0).setQueryHashRetryCount(requests.get(0).getQueryHashRetryCount()-1);
                        }
                        else {
                            header.put(QUERY_HASHING_HEADER, "");
                        }
                        Map<String, String> messageMap = new HashMap<>();
                        messageMap.put("type", "error");
                        messageMap.put("name", CacheHelper.getQueryName(requests.get(0).getQuery()));
                        messageMap.put("key", requests.get(0).getMd5());
                        messageMap.put("hash", queryHashValues.toString());
                        ServerLogger.log(Priority.P1, "GQL_HASHING", messageMap);
                        mApi.getResponse(requests, header, FingerprintManager.getQueryDigest(requests));
                    }
                    if (httpResponse.code() != Const.GQL_RESPONSE_HTTP_OK && httpResponse.body() != null) {
                        LoggingUtils.logGqlResponseCode(httpResponse.code(), requests.toString(), httpResponse.body().toString());
                        return null;
                    }
                    if (httpResponse.body() != null) {
                        LoggingUtils.logGqlSize("java", requests, httpResponse.body().toString());
                    }

                    JsonArray gJsonArray = CommonUtils.getOriginalResponse(httpResponse);

                    return new GraphqlResponseInternal(gJsonArray, false,
                            httpResponse.headers().get(GraphqlConstant.GqlApiKeys.CACHE),
                            httpResponse.headers().get(GraphqlConstant.GqlApiKeys.QUERYHASH));
                }).doOnNext(graphqlResponseInternal -> {
                    //Handling backend cache
                    Map<String, BackendCache> caches = CacheHelper.parseCacheHeaders(graphqlResponseInternal.getBeCache());
                    String[] qhValues = CacheHelper.parseQueryHashHeader(graphqlResponseInternal.getQueryHash());
                    boolean executeCacheFlow = false;
                    boolean executeQueryHashFlow = false;
                    if(qhValues.length > 0){
                        executeQueryHashFlow = true;
                    }
                    if (caches != null && !caches.isEmpty()) {
                        executeCacheFlow = true;
                    }
                        int size = requests.size();
                        for (int i = 0; i < size; i++) {
                            GraphqlRequest request = requests.get(i);
                            if(executeQueryHashFlow){
                                mCacheManager.saveQueryHash(request.getMd5(), qhValues[i]);
                                Map<String, String> messageMap = new HashMap<>();
                                messageMap.put("type", "success");
                                messageMap.put("name", CacheHelper.getQueryName(request.getQuery()));
                                messageMap.put("key", request.getMd5());
                                messageMap.put("hash", qhValues[i]);
                                ServerLogger.log(Priority.P1, "GQL_HASHING", messageMap);
                            }
                            if (request == null || request.isNoCache() || (executeCacheFlow && caches.get(request.getMd5()) == null)) {
                                continue;
                            }

                            JsonElement rawResp = graphqlResponseInternal.getOriginalResponse().get(i);
                            if (rawResp == null
                                    || rawResp.getAsJsonObject() == null
                                    || rawResp.getAsJsonObject().get(GraphqlConstant.GqlApiKeys.DATA) == null) {
                                continue;
                            }

                            JsonElement childResp = rawResp.getAsJsonObject().get(GraphqlConstant.GqlApiKeys.DATA);
                            if (executeCacheFlow && childResp != null) {
                                BackendCache cache = caches.get(request.getMd5());
                                mCacheManager.save(request.cacheKey(), childResp.toString(), cache.getMaxAge() * 1000);
                                Timber.d("Android CLC - Request saved to cache " + CacheHelper.getQueryName(request.getQuery()) + " KEY: " + request.cacheKey());
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
}
