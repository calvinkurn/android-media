package com.tokopedia.common.network.data.source.cloud;

import android.content.Context;
import android.support.annotation.NonNull;

import com.tokopedia.common.network.data.model.RestRequest;
import com.tokopedia.common.network.data.model.RestResponseIntermediate;
import com.tokopedia.common.network.data.source.RestDataStore;
import com.tokopedia.common.network.data.source.cloud.api.RestApi;
import com.tokopedia.common.network.util.CommonUtil;
import com.tokopedia.common.network.util.FingerprintManager;
import com.tokopedia.common.network.util.NetworkClient;
import com.tokopedia.common.network.util.RestCacheManager;
import com.tokopedia.common.network.util.RestConstant;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.converter.StringResponseConverter;
import com.tokopedia.network.interceptor.FingerprintInterceptor;
import com.tokopedia.network.utils.TkpdOkHttpBuilder;
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
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observable;

public class RestCloudDataStore implements RestDataStore {
    private RestApi mApi;
    private RestCacheManager mCacheManager;
    private FingerprintManager mFingerprintManager;

    @Inject
    public RestCloudDataStore() {
        this.mApi = NetworkClient.getApiInterface();
        this.mCacheManager = new RestCacheManager();
        this.mFingerprintManager = NetworkClient.getFingerPrintManager();
    }

    public RestCloudDataStore(List<Interceptor> interceptors, Context context) {
        this.mApi = getApiInterface(interceptors, context);
        this.mCacheManager = new RestCacheManager();
        this.mFingerprintManager = NetworkClient.getFingerPrintManager();
    }

    @Override
    public Observable<RestResponseIntermediate> getResponse(RestRequest request) {
        switch (request.getRequestType()) {
            case GET:
                return doGet(request);
            case POST:
                return doPost(request);
            case PUT:
                return doPut(request);
            case DELETE:
                return delete(request);
            case POST_MULTIPART:
                if (request.getBody() instanceof MultipartBody.Part) {
                    return postMultipart(request);
                } else {
                    return postPartMap(request);

                }
            case PUT_MULTIPART:
                if (request.getBody() instanceof MultipartBody.Part) {
                    return putMultipart(request);
                } else {
                    return putPartMap(request);

                }
            case PUT_REQUEST_BODY:
                if (request.getBody() instanceof RequestBody) {
                    return putRequestBody(request);
                } else {
                    throw new IllegalArgumentException("RequestBody must have params");
                }
            default:
                return doGet(request);
        }
    }

    /**
     * Helper method to Invoke HTTP get request
     *
     * @param request - Request object
     * @return Observable which represent server response
     */
    private Observable<RestResponseIntermediate> doGet(@NonNull RestRequest request) {
        return mApi.get(request.getUrl(), request.getQueryParams(),
                request.getHeaders()).map(response -> processData(request, response));
    }

    /**
     * Helper method to Invoke HTTP post request
     *
     * @param request - Request object
     * @return Observable which represent server response
     */
    private Observable<RestResponseIntermediate> doPost(RestRequest request) {
        if (request.getBody() != null && request.getBody() instanceof Map) {
            return mApi.post(request.getUrl(),
                    (Map<String, Object>) request.getBody(),
                    request.getQueryParams(),
                    request.getHeaders()).map(response -> processData(request, response));
        } else {
            String body = null;
            if (request.getBody() instanceof String) {
                body = (String) request.getBody();
            } else {
                try {
                    body = CommonUtil.toJson(request.getBody());
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
                    request.getHeaders()).map(response -> processData(request, response));
        }
    }

    /**
     * Helper method to Invoke HTTP put request
     *
     * @param request - Request object
     * @return Observable which represent server response
     */
    private Observable<RestResponseIntermediate> doPut(RestRequest request) {
        if (request.getBody() != null && request.getBody() instanceof Map) {
            return mApi.put(request.getUrl(),
                    (Map<String, Object>) request.getBody(),
                    request.getQueryParams(),
                    request.getHeaders()).map(response -> processData(request, response));
        } else {
            String body = null;
            if (request.getBody() instanceof String) {
                body = (String) request.getBody();
            } else {
                try {
                    body = CommonUtil.toJson(request.getBody());
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
                    request.getHeaders()).map(response -> processData(request, response));
        }
    }

    /**
     * Helper method to Invoke HTTP delete request
     *
     * @param request - Request object
     * @return Observable which represent server response
     */
    private Observable<RestResponseIntermediate> delete(@NonNull RestRequest request) {
        return mApi.delete(request.getUrl(),
                request.getQueryParams(),
                request.getHeaders()).map(response -> processData(request, response));
    }

    private Observable<RestResponseIntermediate> postMultipart(RestRequest request) {
        File file = new File(String.valueOf(request.getBody()));
        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("upload", file.getName(), reqFile);

        return mApi.postMultipart(request.getUrl(), multipartBody, request.getQueryParams(), request.getHeaders())
                .map(response -> processData(request, response));
    }

    private Observable<RestResponseIntermediate> postPartMap(RestRequest request) {
        return mApi.postMultipart(request.getUrl(), (Map<String, RequestBody>) request.getBody(), request.getQueryParams(), request.getHeaders())
                .map(response -> processData(request, response));
    }

    private Observable<RestResponseIntermediate> putMultipart(RestRequest request) {
        File file = new File(String.valueOf(request.getBody()));
        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("upload", file.getName(), reqFile);

        return mApi.putMultipart(request.getUrl(), multipartBody, request.getQueryParams(), request.getHeaders())
                .map(response -> processData(request, response));
    }

    private Observable<RestResponseIntermediate> putRequestBody(RestRequest request) {
        return mApi.putRequestBody(request.getUrl(), (RequestBody) request.getBody(), request.getHeaders())
                .map(response -> processData(request, response));
    }

    private Observable<RestResponseIntermediate> putPartMap(RestRequest request) {
        return mApi.putMultipart(request.getUrl(), (Map<String, RequestBody>) request.getBody(), request.getQueryParams(), request.getHeaders())
                .map(response -> processData(request, response));
    }

    /**
     * Helper method to Dump the data into cache
     *
     * @param responseString - Current server response body
     * @param request        - Current server request
     */
    private void cachedData(@NonNull RestRequest request, @NonNull String responseString) {
        //trying to store the data into cache based on cache strategy;
        try {
            switch (request.getCacheStrategy().getType()) {
                case NONE:
                case CACHE_ONLY:
                    //do nothing for now
                    break;
                case CACHE_FIRST:
                case ALWAYS_CLOUD:
                    //store the data into disk
                    mCacheManager.save(mFingerprintManager.generateFingerPrint(request.toString(),
                            request.getCacheStrategy().isSessionIncluded()),
                            responseString,
                            request.getCacheStrategy().getExpiryTime());
            }
        } catch (Exception e) {
            //Just a defencive check in order to avoid any collision between success response.
            e.printStackTrace();
        }
    }

    private RestResponseIntermediate processData(RestRequest request, Response<String> response) {
        RestResponseIntermediate returnResponse;
        try {
            if (response.code() == RestConstant.HTTP_SUCCESS) {
                returnResponse = new RestResponseIntermediate(CommonUtil.fromJson(response.body(), request.getTypeOfT()), request.getTypeOfT(), false);
                returnResponse.setCode(response.code());
                returnResponse.setError(false);

                //For success case saving the data into cache
                cachedData(request, response.body());
            } else {
                //For any kind of error body always be null
                //E.g. error response like HTTP error code = 400,401,410 or 500 etc.
                returnResponse = new RestResponseIntermediate(null, request.getTypeOfT(), false);
                returnResponse.setCode(response.code());
                returnResponse.setErrorBody(response.body() == null ? response.errorBody().string() : response.body());
                returnResponse.setError(true);
            }
        } catch (Exception e) {
            //For any kind of error body always be null
            //E.g. JSONException while serializing json to POJO.
            returnResponse = new RestResponseIntermediate(null, request.getTypeOfT(), false);
            returnResponse.setCode(RestConstant.INTERNAL_EXCEPTION);
            returnResponse.setErrorBody("Caught Exception please fix it--> Responsible class : " + e.getClass().toString() + " Detailed Message: " + e.getMessage() + ", Cause by: " + e.getCause());
            returnResponse.setError(true);
        }
        return returnResponse;
    }

    private RestApi getApiInterface(List<Interceptor> interceptors, Context context) {
        UserSession userSession = new UserSession(context.getApplicationContext());
        TkpdOkHttpBuilder okkHttpBuilder = new TkpdOkHttpBuilder(context, new OkHttpClient.Builder());
        if (interceptors != null) {
            okkHttpBuilder.addInterceptor(new FingerprintInterceptor((NetworkRouter) context.getApplicationContext(), userSession));
            for (Interceptor interceptor : interceptors) {
                if (interceptor == null) {
                    continue;
                }

                okkHttpBuilder.addInterceptor(interceptor);
            }
        }

        return new Retrofit.Builder()
                .baseUrl("http://tokopedia.com/")
                .addConverterFactory(new StringResponseConverter())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okkHttpBuilder.build()).build().create(RestApi.class);
    }
}
