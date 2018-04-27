package com.tokopedia.otp.cotp.data;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.otp.cotp.domain.pojo.ListMethodItemPojo;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * @author by nisie on 4/25/18.
 */

public interface CotpApi {
    String DATE = "Tkpd-UserId";

    @GET(CotpUrl.PATH_GET_METHOD_LIST)
    Observable<Response<DataResponse<ListMethodItemPojo>>> getVerificationMethodList(@QueryMap Map<String,
                Object> parameters);

    @FormUrlEncoded
    @POST(CotpUrl.REQUEST_OTP)
    Observable<Response<String>> requestOtp(@Header(DATE) String date,
                                                    @FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST(CotpUrl.REQUEST_OTP_EMAIL)
    Observable<Response<String>> requestOtpToEmail(@Header(DATE) String date,
                                                         @FieldMap Map<String, Object> parameters);

    @FormUrlEncoded
    @POST(CotpUrl.VALIDATE_OTP)
    Observable<Response<String>> validateOtp(@FieldMap Map<String, Object> param);
}
