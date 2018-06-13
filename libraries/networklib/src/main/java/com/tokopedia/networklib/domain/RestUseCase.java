package com.tokopedia.networklib.domain;

import android.webkit.URLUtil;

import com.google.gson.Gson;
import com.tokopedia.networklib.data.model.RequestType;
import com.tokopedia.networklib.data.model.RestCacheStrategy;
import com.tokopedia.networklib.data.model.RestRequest;
import com.tokopedia.networklib.data.model.RestResponse;
import com.tokopedia.networklib.data.source.repository.RestRepositoryImpl;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import rx.Observable;

public abstract class RestUseCase<T> extends UseCase<RestResponse> {

    private RestRepositoryImpl mRepository;
    protected Type mType;
    private Gson mGson;

    public RestUseCase() {
        mType = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        this.mRepository = new RestRepositoryImpl();
        this.mGson = new Gson();
    }

    @Override
    public Observable<RestResponse> createObservable(RequestParams requestParams) {
        return mRepository.getResponse(buildRequest(), getCacheStrategy()).map(restResponseInternal -> new RestResponse(mGson.fromJson(restResponseInternal.getOriginalResponse(), mType), restResponseInternal.isCached()));
    }

    private RestRequest buildRequest() {
//        if (mType == null) {
//            throw new RuntimeException("Please add valid class type token in order to retrieve the data");
//        }

        if (!URLUtil.isValidUrl(getUrl())) {
            throw new RuntimeException("Please set valid request url into your UseCase class");
        }

        if ((getHttpRequestType() == RequestType.POST
                || getHttpRequestType() == RequestType.DELETE)
                && getBody() == null) {
            throw new RuntimeException("Please set valid request body into your UseCase class");
        }

        return new RestRequest.Builder(mType, getUrl())
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
