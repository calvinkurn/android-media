package com.tokopedia.networklib.data.source.cloud;

import android.content.Context;

import com.google.gson.Gson;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.config.networklibGeneratedDatabaseHolder;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.converter.StringResponseConverter;
import com.tokopedia.network.interceptor.FingerprintInterceptor;
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

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
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

    public CloudRestRestDataStore(List<Interceptor> interceptors, Context context) {
        this.mApi = getApiInterface(interceptors, context);
        this.mGson = new Gson();
        this.mCacheManager = new RestCacheManager();
        this.mFingerprintManager = RestClient.getFingerPrintManager();
    }

    @Override
    public Observable<RestResponseInternal> getResponse(RestRequest request, RestCacheStrategy cacheStrategy) {
        switch (request.getRequestType()) {
            case GET:
                return doGet(request, cacheStrategy);
            case POST:
                return doPost(request, cacheStrategy);
            case PUT:
                return doPut(request, cacheStrategy);
            case DELETE:
                return delete(request, cacheStrategy);
            case POST_MULTIPART:
                return postMultipart(request);
            default:
                return doGet(request, cacheStrategy);
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
     * @param request       - Request object
     * @param cacheStrategy - Caching parameter
     * @return Observable which represent server response
     */
    private Observable<RestResponseInternal> doPost(RestRequest request, RestCacheStrategy cacheStrategy) {
        if (request.getBody() != null && request.getBody() instanceof Map) {
            return mApi.post(request.getUrl(),
                    (Map<String, Object>) request.getBody(),
                    request.getQueryParams(),
                    request.getHeaders()).map(s -> new RestResponseInternal(s, false))
                    .doOnNext(restResponseInternal -> cachedData(cacheStrategy, restResponseInternal, request));
        } else {
            String body = null;
            if (request.getBody() instanceof String) {
                body = (String) request.getBody();
            } else {
                try {
                    body = mGson.toJson(request.getBody());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (body == null) {
                throw new RuntimeException("Invalid json object provided");
            }

            return mApi.post(request.getUrl(),
                    body,
                    request.getQueryParams(),
                    request.getHeaders()).map(s -> new RestResponseInternal(s, false))
                    .doOnNext(restResponseInternal -> cachedData(cacheStrategy, restResponseInternal, request));
        }
    }

    /**
     * Helper method to Invoke HTTP put request
     *
     * @param request       - Request object
     * @param cacheStrategy - Caching parameter
     * @return Observable which represent server response
     */
    private Observable<RestResponseInternal> doPut(RestRequest request, RestCacheStrategy cacheStrategy) {
        if (request.getBody() != null && request.getBody() instanceof Map) {
            return mApi.put(request.getUrl(),
                    (Map<String, Object>) request.getBody(),
                    request.getQueryParams(),
                    request.getHeaders()).map(s -> new RestResponseInternal(s, false))
                    .doOnNext(restResponseInternal -> cachedData(cacheStrategy, restResponseInternal, request));
        } else {
            String body = null;
            if (request.getBody() instanceof String) {
                body = (String) request.getBody();
            } else {
                try {
                    body = mGson.toJson(request.getBody());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (body == null) {
                throw new RuntimeException("Invalid json object provided");
            }

            return mApi.post(request.getUrl(),
                    body,
                    request.getQueryParams(),
                    request.getHeaders()).map(s -> new RestResponseInternal(s, false))
                    .doOnNext(restResponseInternal -> cachedData(cacheStrategy, restResponseInternal, request));
        }
    }

    /**
     * Helper method to Invoke HTTP delete request
     *
     * @param request       - Request object
     * @param cacheStrategy - Caching parameter
     * @return Observable which represent server response
     */
    private Observable<RestResponseInternal> delete(RestRequest request, RestCacheStrategy cacheStrategy) {
        return mApi.delete(request.getUrl(),
                request.getQueryParams(),
                request.getHeaders()).map(s -> new RestResponseInternal(s, false))
                .doOnNext(restResponseInternal -> cachedData(cacheStrategy, restResponseInternal, request));
    }

    private Observable<RestResponseInternal> postMultipart(RestRequest request) {
        File file = new File(String.valueOf(request.getBody()));
        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("upload", file.getName(), reqFile);

        return mApi.postMultipart(request.getUrl(), multipartBody, request.getQueryParams(), request.getHeaders())
                .map(response -> new RestResponseInternal(response.body(), response.code(), false));
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

    private RestApi getApiInterface(List<Interceptor> interceptors, Context context) {
        UserSession userSession = new UserSession(context.getApplicationContext());
        FlowManager.initModule(networklibGeneratedDatabaseHolder.class);
        TkpdOkHttpBuilder okkHttpBuilder = new TkpdOkHttpBuilder(context, new OkHttpClient.Builder());
        okkHttpBuilder.addInterceptor(new FingerprintInterceptor((NetworkRouter) context.getApplicationContext(), userSession));
        for (Interceptor interceptor : interceptors) {
            if (interceptor == null) {
                continue;
            }

            okkHttpBuilder.addInterceptor(interceptor);
        }

        return new Retrofit.Builder()
                .baseUrl("http://tokopedia.com/")
                .addConverterFactory(new StringResponseConverter())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okkHttpBuilder.build()).build().create(RestApi.class);
    }
}
