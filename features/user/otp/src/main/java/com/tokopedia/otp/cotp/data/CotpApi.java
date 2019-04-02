package com.tokopedia.otp.cotp.data;

import com.tokopedia.otp.common.network.WsResponse;
import com.tokopedia.otp.cotp.domain.pojo.RequestOtpPojo;
import com.tokopedia.otp.cotp.domain.pojo.ValidateOtpPojo;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @author by nisie on 4/25/18.
 */

public interface CotpApi {
    String DATE = "Date";
    String USER_ID = "Tkpd-UserId";

    @FormUrlEncoded
    @POST(CotpUrl.REQUEST_OTP)
    Observable<Response<WsResponse<RequestOtpPojo>>> requestOtp(@Header(DATE) String date,
                                                                @Header(USER_ID) String userId,
                                                                @FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST(CotpUrl.REQUEST_OTP_EMAIL)
    Observable<Response<WsResponse<RequestOtpPojo>>> requestOtpEmail(@Header(DATE) String date,
                                                                     @Header(USER_ID) String userId,
                                                                     @FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST(CotpUrl.VALIDATE_OTP)
    Observable<Response<WsResponse<ValidateOtpPojo>>> validateOtp(
            @FieldMap Map<String, Object> param);
}
