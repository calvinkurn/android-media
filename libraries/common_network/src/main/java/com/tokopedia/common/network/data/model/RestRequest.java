package com.tokopedia.common.network.data.model;

import android.webkit.URLUtil;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;

public class RestRequest {

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
    private Type typeOfT;

    /**
     * Mandatory implementation- Valid implementation require
     * Full URL of the endpoint including base url. This value is mandatory. Url should be valid else UseCase wil throw exception (RuntimeException("Please set valid request url into your UseCase class"))
     *
     * @return Map -> Full URL of the endpoint (e.g. https://tokopedia.com/your/path/method/xyz)
     */
    private String url;

    /**
     * Optional
     * For providing extra headers to the apis.
     *
     * @return Key-Value pair of header
     */
    private Map<String, Object> headers;

    /**
     * Optional
     * For providing query parameter to the apis.
     *
     * @return Map -> Key-Value pair of query parameter (No need to encode, library will take care of this)
     */
    private Map<String, Object> queryParams;

    /**
     * Optional
     * For providing form parameter to the apis.
     *
     * @return Map -> Key-Value pair of query parameter.
     */
    private Map<String, RequestBody> parts;

    /**
     * Optional
     * For providing Http method type, by default GET will be treated if not provided any method.
     *
     * @return RequestType enum  (e.g RequestType.GET, RequestType.POST, RequestType.PUT, RequestType.DELETE)
     * default is RequestType.GET if you will return null
     */
    private RequestType requestType;

    /**
     * Mandatory implementation (If Method type is POST or PUT else it Optional)- Valid implementation require
     * For providing query params to the apis.
     *
     * @return Return argument can any one of from below
     * 1. Map Object -->Key-Value pair of query. (For content-type -> @FormUrlEncoded) (No need to encode, library will take care of this)
     * 2. String --> Any string which can be become part of body. (For content-type -> application/json)
     * 3. Java POJO class object -> which can be serialize and deserialize later. (For content-type -> application/json)
     * 4. String --> Path of the file which are going to upload. (For content-type -> @Multipart)
     * <p>
     * If you will trying to return other then above object, then it will trow exception later while executing the network request
     */
    private Object body;

    /**
     * Optional
     * For providing CacheStrategy, by Default no caching will be perform if not provided
     *
     * @return Object - RestCacheStrategy (RestCacheStrategy.Builder to create your RestCacheStrategy)
     * Default is NONE caching
     */
    private RestCacheStrategy cacheStrategy;

    private RestRequest(Builder builder) {
        this.typeOfT = builder.typeOfT;
        this.url = builder.url;
        this.headers = builder.headers == null ? new HashMap<>() : builder.headers;
        this.queryParams = builder.queryParams == null ? new HashMap<>() : builder.queryParams;
        this.requestType = builder.requestType == null ? RequestType.GET : builder.requestType;
        this.body = builder.body;
        this.cacheStrategy = builder.cacheStrategy == null ? new RestCacheStrategy.Builder(CacheType.NONE).build() : builder.cacheStrategy;

        if (builder.typeOfT == null) {
            throw new RuntimeException("T class type cannot be null.");
        }

        if (!URLUtil.isValidUrl(builder.url)) {
            throw new RuntimeException("Invalid url.");
        }

        if ((builder.requestType == RequestType.POST
                || builder.requestType == RequestType.PUT)
                && builder.body == null) {
            throw new IllegalArgumentException("BODY cannot be null, for method type POST or PUT.");
        }
    }

    public Type getTypeOfT() {
        return typeOfT;
    }

    public String getUrl() {
        return url;
    }

    public Map<String, Object> getHeaders() {
        return headers;
    }

    public Map<String, Object> getQueryParams() {
        return queryParams;
    }

    public Map<String, RequestBody> getPartMap() {
        return parts;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public Object getBody() {
        return body;
    }

    public RestCacheStrategy getCacheStrategy() {
        return cacheStrategy;
    }

    //Builder class
    public static class Builder {
        private Type typeOfT; /* Mandatory parameter */
        private String url; /* Mandatory parameter */

        private Map<String, Object> headers;
        private Map<String, Object> queryParams;
        private RequestType requestType;
        private Object body;  /* Mandatory parameter of RequestType is GET or POST */
        private RestCacheStrategy cacheStrategy;

        public Builder(String url, Type classOfT) {
            this.typeOfT = classOfT;
            this.url = url;
        }

        public Builder setHeaders(Map<String, Object> headers) {
            this.headers = headers;
            return this;
        }

        public Builder setQueryParams(Map<String, Object> queryParams) {
            this.queryParams = queryParams;
            return this;
        }

        public Builder setRequestType(RequestType requestType) {
            this.requestType = requestType;
            return this;
        }

        public Builder setBody(Object body) {
            this.body = body;
            return this;
        }

        public Builder setCacheStrategy(RestCacheStrategy cacheStrategy) {
            this.cacheStrategy = cacheStrategy;
            return this;
        }

        public RestRequest build() {
            return new RestRequest(this);
        }
    }

    @Override
    public String toString() {
        return "RestRequest{" +
                ", url='" + url + '\'' +
                ", headers=" + headers +
                ", queryParams=" + queryParams +
                ", requestType=" + requestType +
                ", body=" + body +
                '}';
    }
}
