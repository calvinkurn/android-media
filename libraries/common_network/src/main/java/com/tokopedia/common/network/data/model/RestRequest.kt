package com.tokopedia.common.network.data.model

import android.webkit.URLUtil

import java.lang.reflect.Type
import java.util.HashMap

import okhttp3.RequestBody

class RestRequest private constructor(builder: Builder) {

    companion object {
        // In WS api, query key device_id, hash, and device_time will change over time
        val unCacheableKeyList by lazy {
            mutableListOf("device_time", "hash", "device_id")
        }
    }

    /**
     * Mandatory implementation - Valid implementation require
     * typeOfT The specific genericized type of src. You can obtain this type by using the
     * [com.google.gson.reflect.TypeToken] class. For example, to get the type for
     * `Collection<Foo>`, you should use:
     * <pre>
     * Type typeOfT = new TypeToken&lt;Collection&lt;Foo&gt;&gt;(){}.getType();
    </pre> *
     *
     * @return Class type (e.g. TestModel.class or XYZ.class)
     */
    val typeOfT: Type

    /**
     * Mandatory implementation- Valid implementation require
     * Full URL of the endpoint including base url. This value is mandatory. Url should be valid else UseCase wil throw exception (RuntimeException("Please set valid request url into your UseCase class"))
     *
     * @return Map -> Full URL of the endpoint (e.g. https://tokopedia.com/your/path/method/xyz)
     */
    val url: String

    /**
     * Optional
     * For providing extra headers to the apis.
     *
     * @return Key-Value pair of header
     */
    val headers: Map<String, String>?

    /**
     * Optional
     * For providing query parameter to the apis.
     *
     * @return Map -> Key-Value pair of query parameter (No need to encode, library will take care of this)
     */
    val queryParams: Map<String, String>?
    /**
     * Optional
     * For providing form parameter to the apis.
     *
     * @return Map -> Key-Value pair of query parameter.
     */
    val partMap: Map<String, RequestBody>? = null

    /**
     * Optional
     * For providing Http method type, by default GET will be treated if not provided any method.
     *
     * @return RequestType enum  (e.g RequestType.GET, RequestType.POST, RequestType.PUT, RequestType.DELETE)
     * default is RequestType.GET if you will return null
     */
    val requestType: RequestType?

    /**
     * Mandatory implementation (If Method type is POST or PUT else it Optional)- Valid implementation require
     * For providing query params to the apis.
     *
     * @return Return argument can any one of from below
     * 1. Map Object -->Key-Value pair of query. (For content-type -> @FormUrlEncoded) (No need to encode, library will take care of this)
     * 2. String --> Any string which can be become part of body. (For content-type -> application/json)
     * 3. Java POJO class object -> which can be serialize and deserialize later. (For content-type -> application/json)
     * 4. String --> Path of the file which are going to upload. (For content-type -> @Multipart)
     *
     *
     * If you will trying to return other then above object, then it will trow exception later while executing the network request
     */
    val body: Any?

    /**
     * Optional
     * For providing CacheStrategy, by Default no caching will be perform if not provided
     *
     * @return Object - RestCacheStrategy (RestCacheStrategy.Builder to create your RestCacheStrategy)
     * Default is NONE caching
     */
    val cacheStrategy: RestCacheStrategy

    init {
        this.typeOfT = builder.typeOfT
        this.url = builder.url
        this.headers = if (builder._headers == null) HashMap() else builder._headers
        this.queryParams = if (builder._queryParams == null) HashMap() else {
            val newMap = mutableMapOf<String, String>()
            builder._queryParams?.run {
                for ((key, value) in this) {
                    if (value is String) {
                        newMap.put(key, value)
                    } else {
                        newMap.put(key, value.toString())
                    }
                }
            }
            newMap
        }
        this.requestType = if (builder._requestType == null) RequestType.GET else builder._requestType
        this.body = builder._body
        this.cacheStrategy = builder._cacheStrategy

        if (!URLUtil.isValidUrl(builder.url)) {
            throw RuntimeException("Invalid url.")
        }

        if ((builder._requestType == RequestType.POST || builder._requestType == RequestType.PUT) && builder._body == null) {
            throw IllegalArgumentException("BODY cannot be null, for method type POST or PUT.")
        }
    }

    //Builder class
    class Builder(val url: String /* Mandatory parameter */, val typeOfT: Type /* Mandatory parameter */) {

        var _headers: Map<String, String>? = null
        var _queryParams: Map<String, Any>? = null
        var _requestType: RequestType? = null
        var _body: Any? = null  /* Mandatory parameter of RequestType is GET or POST */
        var _cacheStrategy: RestCacheStrategy = RestCacheStrategy.Builder(CacheType.NONE).build()

        fun setHeaders(headers: Map<String, String>): Builder {
            this._headers = headers
            return this
        }

        fun setQueryParams(queryParams: Map<String, Any>): Builder {
            this._queryParams = queryParams
            return this
        }

        fun setRequestType(requestType: RequestType): Builder {
            this._requestType = requestType
            return this
        }

        fun setBody(body: Any): Builder {
            this._body = body
            return this
        }

        fun setCacheStrategy(cacheStrategy: RestCacheStrategy): Builder {
            this._cacheStrategy = cacheStrategy
            return this
        }

        fun build(): RestRequest {
            return RestRequest(this)
        }
    }

    /**
     * In WS api, query key device_id, hash, and device_time will change over time
     */
    private fun Map<String, Any>.trim() {
        var string: String = ""
        for ((key, value) in this) {
            if (key !in unCacheableKeyList) {
                string += "$key=$value;"
            }
        }
    }

    override fun toString(): String {
        return "RestRequest{" +
                ", url='" + url + '\''.toString() +
                ", headers=" + headers +
                ", queryParams=" + (queryParams?.trim() ?: "") +
                ", requestType=" + requestType +
                ", body=" + body +
                '}'.toString()
    }
}
