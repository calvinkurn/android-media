package com.tokopedia.loginphone.common.data;

import com.tokopedia.loginphone.checkloginphone.domain.pojo.CheckMsisdnTokoCashPojo;
import com.tokopedia.loginphone.choosetokocashaccount.data.GetCodeTokoCashPojo;
import com.tokopedia.loginphone.verifyotptokocash.domain.pojo.requestotp.RequestOtpTokoCashPojo;
import com.tokopedia.loginphone.verifyotptokocash.domain.pojo.verifyotp.VerifyOtpTokoCashPojo;
import com.tokopedia.network.data.model.response.DataResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by nabillasabbaha on 10/11/17.
 */

public interface LoginRegisterPhoneApi {

    @FormUrlEncoded
    @POST(LoginRegisterPhoneUrl.REQUEST_OTP_LOGIN)
    Observable<Response<DataResponse<RequestOtpTokoCashPojo>>> requestLoginOtp(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST(LoginRegisterPhoneUrl.VERIFY_OTP_LOGIN)
    Observable<Response<DataResponse<VerifyOtpTokoCashPojo>>> verifyLoginOtp(@FieldMap Map<String,
            Object> parameters);

    @FormUrlEncoded
    @POST(LoginRegisterPhoneUrl.AUTHORIZE)
    Observable<Response<DataResponse<GetCodeTokoCashPojo>>> getAuthorizeCode(@FieldMap Map<String,
            Object> parameters);

    @FormUrlEncoded
    @POST(LoginRegisterPhoneUrl.CHECK_MSISDN)
    Observable<Response<DataResponse<CheckMsisdnTokoCashPojo>>> checkMsisdn(@FieldMap Map<String,
            Object> parameters);
}
