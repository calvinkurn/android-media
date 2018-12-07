package com.tokopedia.loginphone.checkregisterphone.data;

import com.tokopedia.loginphone.checkregisterphone.domain.pojo.CheckRegisterPhoneNumberPojo;
import com.tokopedia.network.data.model.response.DataResponse;

import java.util.HashMap;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @author by nisie on 31/10/18.
 */
public interface CheckMsisdnApi {

    @FormUrlEncoded
    @POST(CheckMsisdnUrl.PATH_REGISTER_MSISDN_CHECK)
    Observable<Response<DataResponse<CheckRegisterPhoneNumberPojo>>> checkMsisdnRegisterPhoneNumber(@FieldMap HashMap<String, Object> parameters);

}
