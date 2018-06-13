package com.tokopedia.networklib.domain;

import android.content.Context;
import android.webkit.URLUtil;

import com.google.gson.Gson;
import com.tokopedia.networklib.data.model.RequestType;
import com.tokopedia.networklib.data.model.RestCacheStrategy;
import com.tokopedia.networklib.data.model.RestRequest;
import com.tokopedia.networklib.data.model.RestResponse;
import com.tokopedia.networklib.data.source.repository.RestRepositoryImpl;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Interceptor;
import rx.Observable;

public abstract class RestUseCaseSupportInterceptor extends UseCase<RestResponse> {

    private RestRepositoryImpl mRepository;
    private Gson mGson;

    public RestUseCaseSupportInterceptor(Interceptor interceptor, Context context) {
        this.mRepository = new RestRepositoryImpl(Arrays.asList(interceptor), context);
        this.mGson = new Gson();
    }

    public RestUseCaseSupportInterceptor(List<Interceptor> interceptors, Context context) {
        this.mRepository = new RestRepositoryImpl(interceptors, context);
        this.mGson = new Gson();
    }

    @Override
    public Observable<RestResponse> createObservable(RequestParams requestParams) {
        return mRepository.getResponse(buildRequest(), getCacheStrategy()).map(restResponseInternal ->
                new RestResponse(mGson.fromJson(restResponseInternal.getOriginalResponse(), getTypeOfT()), restResponseInternal.isCached()));
    }

    private RestRequest buildRequest() {
        if (getTypeOfT() == null) {
            throw new RuntimeException("Please add valid class type token in order to retrieve the data");
        }

        if (!URLUtil.isValidUrl(getUrl())) {
            throw new RuntimeException("Please set valid request url into your UseCase class");
        }

        if ((getHttpRequestType() == RequestType.POST
                || getHttpRequestType() == RequestType.PUT)
                && getBody() == null) {
            throw new RuntimeException("Please set valid request body into your UseCase class");
        }

        return new RestRequest.Builder(getTypeOfT(), getUrl())
                .setBody(getBody())
                .setRequestType(getHttpRequestType() == null ? RequestType.GET : getHttpRequestType())
                .setHeaders(getHeaders() == null ? new HashMap<>() : getHeaders())
                .setQueryParams(getQueriesMap() == null ? new HashMap<>() : getQueriesMap())
                .build();
    }

    /**
     * Mandatory implementation- Valid implementation require
     * Full URL of the endpoint including base url. This value is mandatory. Url should be valid else UseCase wil throw exception (RuntimeException("Please set valid request url into your UseCase class"))
     *
     * @return Map -> Full URL of the endpoint
     */
    public abstract String getUrl();

    /**
     * Mandatory implementation - Valid implementation require
     * typeOfT The specific genericized type of src. You can obtain this type by using the
     * {@link com.google.gson.reflect.TypeToken} class. For example, to get the type for
     * {@code Collection<Foo>}, you should use:
     * <pre>
     * Type typeOfT = new TypeToken&lt;Collection&lt;Foo&gt;&gt;(){}.getType();
     * </pre>
     *
     * @return Class type
     */
    public abstract Type getTypeOfT();


    /**
     * Optional
     * For providing extra headers to the apis.
     *
     * @return Key-Value pair of header
     */
    public abstract Map<String, Object> getHeaders();

    /**
     * Optional
     * For providing query params to the apis.
     *
     * @return Map -> Key-Value pair of query
     */
    public abstract Map<String, Object> getQueriesMap();

    /**
     * Mandatory implementation (If Method type is POST or PUT else it Optional)- Valid implementation require
     * For providing query params to the apis.
     *
     * @return Map -> Key-Value pair of query
     */
    public abstract Object getBody();

    /**
     * Optional
     * For providing Http method type, by default GET will be treated if not provided any method.
     *
     * @return Map -> RequestType enum
     */
    public abstract RequestType getHttpRequestType();

    /**
     * Optional
     * For providing CacheStrategy, by Default no caching will be perform if not provided
     *
     * @return Object - RestCacheStrategy
     */
    public abstract RestCacheStrategy getCacheStrategy();
}
