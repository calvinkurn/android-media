package com.tokopedia.core.base.common.service;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

/**
 * @author ricoharisin .
 */

public interface CommonService {

    @GET
    Observable<String> get(@Url String Url, @QueryMap TKPDMapParam<String, Object> params);

    @POST
    Observable<String> post(@Url String Url, @FieldMap TKPDMapParam<String, Object> params);

}
