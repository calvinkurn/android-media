package com.tokopedia.search.result.network.service

import retrofit2.http.GET
import com.tokopedia.discovery.common.constants.SearchConstant
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.QueryMap
import rx.Observable

interface BrowseApi {
    @GET(SearchConstant.Ace.PATH_BROWSE_CATALOG)
    fun browseCatalogs(
            @QueryMap requestParams: Map<String?, Any?>?
    ): Observable<Response<String?>?>?

    @GET(SearchConstant.Ace.PATH_GET_DYNAMIC_ATTRIBUTE)
    fun getDynamicAttribute(
            @QueryMap requestParams: Map<String?, Any?>?
    ): Observable<Response<String?>?>?

    @GET(SearchConstant.Ace.PATH_GET_DYNAMIC_ATTRIBUTE_V4)
    fun getDynamicAttributeV4(
            @QueryMap requestParams: Map<String?, Any?>?
    ): Observable<Response<String?>?>?

    @GET(SearchConstant.Ace.PATH_GET_DYNAMIC_ATTRIBUTE)
    fun getDynamicAttributeDeferred(
            @QueryMap requestParams: Map<String?, Any?>?
    ): Deferred<Response<String?>?>?

    @GET(SearchConstant.Ace.PATH_GET_DYNAMIC_ATTRIBUTE_V4)
    fun getDynamicAttributeV4Deferred(
            @QueryMap requestParams: Map<String?, Any?>?
    ): Deferred<Response<String?>?>?
}