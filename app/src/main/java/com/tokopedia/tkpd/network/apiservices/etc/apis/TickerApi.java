package com.tokopedia.tkpd.network.apiservices.etc.apis;

import com.tokopedia.tkpd.network.constants.TkpdBaseURL;
import com.tokopedia.tkpd.network.retrofit.response.TkpdResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * TickerApi
 * Created by Angga.Prasetiyo on 08/12/2015.
 */
public interface TickerApi {

    @FormUrlEncoded
    @POST(TkpdBaseURL.Etc.PATH_GET_DATA_SOURCE_TICKER)
    Observable<Response<TkpdResponse>> getDataSource(@FieldMap Map<String, String> params);
}
