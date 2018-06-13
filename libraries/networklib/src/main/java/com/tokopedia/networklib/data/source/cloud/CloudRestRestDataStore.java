package com.tokopedia.networklib.data.source.cloud;

import android.content.Context;

import com.google.gson.Gson;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.config.networklibGeneratedDatabaseHolder;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.converter.StringResponseConverter;
import com.tokopedia.network.interceptor.FingerprintInterceptor;
import com.tokopedia.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.network.utils.TkpdOkHttpBuilder;
import com.tokopedia.networklib.data.model.RestCacheStrategy;
import com.tokopedia.networklib.data.model.RestRequest;
import com.tokopedia.networklib.data.model.RestResponseInternal;
import com.tokopedia.networklib.data.source.RestDataStore;
import com.tokopedia.networklib.data.source.cloud.api.RestApi;
import com.tokopedia.networklib.util.FingerprintManager;
import com.tokopedia.networklib.util.RestCacheManager;
import com.tokopedia.networklib.util.RestClient;
import com.tokopedia.user.session.UserSession;

import java.util.Map;

import javax.inject.Inject;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observable;

public class CloudRestRestDataStore implements RestDataStore {

    private RestApi mApi;
    private Gson mGson;
    private RestCacheManager mCacheManager;
    private FingerprintManager mFingerprintManager;

    @Inject
    public CloudRestRestDataStore() {
        this.mApi = RestClient.getApiInterface();
        this.mGson = new Gson();
        this.mCacheManager = new RestCacheManager();
        this.mFingerprintManager = RestClient.getFingerPrintManager();
    }

    public CloudRestRestDataStore(Interceptor interceptor, Context context) {
        this.mApi = getApiInterface(interceptor, context);
        this.mGson = new Gson();
        this.mCacheManager = new RestCacheManager();
        this.mFingerprintManager = RestClient.getFingerPrintManager();
    }

    @Override
    public Observable<RestResponseInternal> getResponse(RestRequest requests, RestCacheStrategy cacheStrategy) {
        switch (requests.getRequestType()) {
            case GET:
                return doGet(requests, cacheStrategy);
            case POST:
                return doPost(requests, cacheStrategy);
            case PUT:
                return doPut(requests, cacheStrategy);
            case DELETE:
                return delete(requests, cacheStrategy);
            default:
                return doGet(requests, cacheStrategy);
        }
    }

    /**
     * Helper method to Invoke HTTP get request
     *
     * @param requests      - Request object
     * @param cacheStrategy - Caching parameter
     * @return Observable which represent server response
     */
    private Observable<RestResponseInternal> doGet(RestRequest requests, RestCacheStrategy cacheStrategy) {
        return mApi.get(requests.getUrl(),
                requests.getQueryParams(),
                requests.getHeaders()).map(s -> new RestResponseInternal(s, false))
                .doOnNext(restResponseInternal -> cachedData(cacheStrategy, restResponseInternal, requests));
    }

    /**
     * Helper method to Invoke HTTP post request
     *
     * @param requests      - Request object
     * @param cacheStrategy - Caching parameter
     * @return Observable which represent server response
     */
    private Observable<RestResponseInternal> doPost(RestRequest requests, RestCacheStrategy cacheStrategy) {
        if (requests.getBody() != null && requests.getBody() instanceof Map) {
            return mApi.post(requests.getUrl(),
                    (Map<String, Object>) requests.getBody(),
                    requests.getQueryParams(),
                    requests.getHeaders()).map(s -> new RestResponseInternal(s, false))
                    .doOnNext(restResponseInternal -> cachedData(cacheStrategy, restResponseInternal, requests));
        } else {
            String body = null;
            if (requests.getBody() instanceof String) {
                body = (String) requests.getBody();
            } else {
                try {
                    body = mGson.toJson(requests.getBody());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (body == null) {
                throw new RuntimeException("Invalid json object provided");
            }

            return mApi.post(requests.getUrl(),
                    body,
                    requests.getQueryParams(),
                    requests.getHeaders()).map(s -> new RestResponseInternal(s, false))
                    .doOnNext(restResponseInternal -> cachedData(cacheStrategy, restResponseInternal, requests));
        }
    }

    /**
     * Helper method to Invoke HTTP put request
     *
     * @param requests      - Request object
     * @param cacheStrategy - Caching parameter
     * @return Observable which represent server response
     */
    private Observable<RestResponseInternal> doPut(RestRequest requests, RestCacheStrategy cacheStrategy) {
        if (requests.getBody() != null && requests.getBody() instanceof Map) {
            return mApi.put(requests.getUrl(),
                    (Map<String, Object>) requests.getBody(),
                    requests.getQueryParams(),
                    requests.getHeaders()).map(s -> new RestResponseInternal(s, false))
                    .doOnNext(restResponseInternal -> cachedData(cacheStrategy, restResponseInternal, requests));
        } else {
            String body = null;
            if (requests.getBody() instanceof String) {
                body = (String) requests.getBody();
            } else {
                try {
                    body = mGson.toJson(requests.getBody());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (body == null) {
                throw new RuntimeException("Invalid json object provided");
            }

            return mApi.post(requests.getUrl(),
                    body,
                    requests.getQueryParams(),
                    requests.getHeaders()).map(s -> new RestResponseInternal(s, false))
                    .doOnNext(restResponseInternal -> cachedData(cacheStrategy, restResponseInternal, requests));
        }
    }

    /**
     * Helper method to Invoke HTTP delete request
     *
     * @param requests      - Request object
     * @param cacheStrategy - Caching parameter
     * @return Observable which represent server response
     */
    private Observable<RestResponseInternal> delete(RestRequest requests, RestCacheStrategy cacheStrategy) {
        return mApi.delete(requests.getUrl(),
                requests.getQueryParams(),
                requests.getHeaders()).map(s -> new RestResponseInternal(s, false))
                .doOnNext(restResponseInternal -> cachedData(cacheStrategy, restResponseInternal, requests));
    }

    /**
     * Helper method to Dump the data into cache
     *
     * @param cacheStrategy - CacheStrategy
     * @param response      - Current server response
     * @param request       - Current server request
     */
    private void cachedData(RestCacheStrategy cacheStrategy, RestResponseInternal response, RestRequest request) {
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
                    mCacheManager.save(mFingerprintManager.generateFingerPrint(request.toString(), cacheStrategy.isSessionIncluded()),
                            response.getOriginalResponse(),
                            cacheStrategy.getExpiryTime());
            }
        }
    }

    private RestApi getApiInterface(Interceptor interceptor, Context context) {
        UserSession userSession = new UserSession(context.getApplicationContext());
        FlowManager.initModule(networklibGeneratedDatabaseHolder.class);
        TkpdOkHttpBuilder tkpdOkHttpBuilder = new TkpdOkHttpBuilder(context, new OkHttpClient.Builder());
        tkpdOkHttpBuilder.addInterceptor(new TkpdAuthInterceptor(context, (NetworkRouter) context.getApplicationContext(), userSession));
        tkpdOkHttpBuilder.addInterceptor(new FingerprintInterceptor((NetworkRouter) context.getApplicationContext(), userSession));
        tkpdOkHttpBuilder.addInterceptor(interceptor);

        return new Retrofit.Builder()
                .baseUrl("http://tokopedia.com/")
                .addConverterFactory(new StringResponseConverter())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(tkpdOkHttpBuilder.build()).build().create(RestApi.class);
    }
}
