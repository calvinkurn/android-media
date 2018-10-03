package com.tokopedia.changephonenumber.data.api;

import com.tokopedia.abstraction.common.network.response.TokopediaWsV4Response;
import com.tokopedia.changephonenumber.ChangePhoneNumberUrl;
import com.tokopedia.changephonenumber.data.model.ValidateOtpStatusData;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * @author by alvinatin on 26/09/18.
 */

public interface ChangePhoneNumberApi {

    @GET(ChangePhoneNumberUrl.ChangeMSISDN.GET_WARNING)
    Observable<Response<TokopediaWsV4Response>> getWarning(@QueryMap HashMap<String, Object> params);

    @FormUrlEncoded
    @POST(ChangePhoneNumberUrl.ChangeMSISDN.VALIDATE)
    Observable<Response<TokopediaWsV4Response>> validateNumber(@FieldMap HashMap<String, Object> params);

    @GET(ChangePhoneNumberUrl.OTP.VALIDATE_OTP_STATUS)
    Observable<Response<ValidateOtpStatusData>> validateOtpStatus(@QueryMap Map<String, Object>
                                                                          parameters);
}
