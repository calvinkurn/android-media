package com.tokopedia.core.base.common.service;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

/**
 * @author ricoharisin .
 */

public interface CommonService {

    @GET
    Observable<String> get(@Url String Url, @QueryMap Map<String, String> params);

    @FormUrlEncoded
    @POST
    Observable<String> post(@Url String Url, @FieldMap Map<String, String> params);

    @POST
    Observable<String> post(@Url String Url);

    @DELETE
    Observable<String> delete(@Url String Url);

    @GET
    Observable<String> get(@Url String Url);

}
