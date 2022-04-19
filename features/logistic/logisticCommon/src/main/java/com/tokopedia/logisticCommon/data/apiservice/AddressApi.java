package com.tokopedia.logisticCommon.data.apiservice;

import com.tokopedia.abstraction.common.network.response.TokopediaWsV4Response;
import com.tokopedia.network.constant.TkpdBaseURL;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Fajar Ulin Nuha on 11/10/18.
 */
public interface AddressApi {

    @FormUrlEncoded
    @POST(TkpdBaseURL.Etc.PATH_GET_CITY)
    Observable<Response<TokopediaWsV4Response>> getCity(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Etc.PATH_GET_DISTRICT)
    Observable<Response<TokopediaWsV4Response>> getDistrict(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Etc.PATH_GET_PROVINCE)
    Observable<Response<TokopediaWsV4Response>> getProvince(@FieldMap Map<String, String> params);

}
