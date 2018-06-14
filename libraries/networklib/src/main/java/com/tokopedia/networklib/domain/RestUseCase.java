package com.tokopedia.networklib.domain;

import android.webkit.URLUtil;

import com.google.gson.Gson;
import com.tokopedia.networklib.data.model.CacheType;
import com.tokopedia.networklib.data.model.RequestType;
import com.tokopedia.networklib.data.model.RestCacheStrategy;
import com.tokopedia.networklib.data.model.RestRequest;
import com.tokopedia.networklib.data.model.RestResponse;
import com.tokopedia.networklib.data.source.repository.RestRepositoryImpl;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import rx.Observable;

public abstract class RestUseCase extends UseCase<RestResponse> {

    private RestRepositoryImpl mRepository;
    private Gson mGson;

    public RestUseCase() {
        this.mRepository = new RestRepositoryImpl();
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
     * @return Map -> Full URL of the endpoint (e.g. https://tokopedia.com/your/path/method/xyz)
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
     * @return Class type (e.g. TestModel.class or XYZ.class)
     */
    protected abstract Type getTypeOfT();


    /**
     * Optional
     * For providing extra headers to the apis.
     *
     * @return Key-Value pair of header
     */
    protected Map<String, Object> getHeaders() {
        return null;
    }

    /**
     * Optional
     * For providing query parameter to the apis.
     *
     * @return Map -> Key-Value pair of query parameter (No need to encode, library will take care of this)
     */
    protected Map<String, Object> getQueriesMap() {
        return null;
    }

    /**
     * Mandatory implementation (If Method type is POST or PUT else it Optional)- Valid implementation require
     * For providing query params to the apis.
     *
     * @return Return argument can any one of from below
     * 1. Map Object -->Key-Value pair of query. (For content-type -> @FormUrlEncoded) (No need to encode, library will take care of this)
     * 2. String --> Any string which can be become part of body. (For content-type -> application/json)
     * 3. Java POJO class object -> which can be serialize and deserialize later. (For content-type -> application/json)
     * <p>
     * If you will trying to return other then above object, then it will trow exception later while executing the network request
     */
    protected Object getBody() {
        return null;
    }

    /**
     * Optional
     * For providing Http method type, by default GET will be treated if not provided any method.
     *
     * @return RequestType enum  (e.g RequestType.GET, RequestType.POST, RequestType.PUT, RequestType.DELETE)
     * default is RequestType.GET if you will return null
     */
    protected RequestType getHttpRequestType() {
        return RequestType.GET;
    }

    /**
     * Optional
     * For providing CacheStrategy, by Default no caching will be perform if not provided
     *
     * @return Object - RestCacheStrategy (RestCacheStrategy.Builder to create your RestCacheStrategy)
     * Default is NONE caching
     */
    protected RestCacheStrategy getCacheStrategy() {
        return new RestCacheStrategy.Builder(CacheType.NONE).build();
    }
}
