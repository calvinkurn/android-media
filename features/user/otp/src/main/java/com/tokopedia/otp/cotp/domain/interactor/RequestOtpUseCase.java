package com.tokopedia.otp.cotp.domain.interactor;

import com.tokopedia.otp.cotp.domain.source.OtpSource;
import com.tokopedia.otp.cotp.view.viewmodel.RequestOtpViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by nisie on 10/21/17.
 *         params :
 *         - mode : use static parameter from RequestOtpUseCase
 *         - otp_type : use static parameter from RequestOtpUseCase
 */

public class RequestOtpUseCase extends UseCase<RequestOtpViewModel> {

    protected static final String PARAM_MODE = "mode";
    protected static final String PARAM_OTP_TYPE = "otp_type";
    protected static final String PARAM_MSISDN = "msisdn";
    protected static final String PARAM_EMAIL = "user_email";
    public static final String PARAM_USER_ID = "user";
    private static final String PARAM_OS_TYPE = "os_type";
    private static final String PARAM_TIMESTAMP = "device_time";

    public static final String MODE_SMS = "sms";
    public static final String MODE_CALL = "call";
    public static final String MODE_EMAIL = "email";

    public static final int OTP_TYPE_SECURITY_QUESTION = 13;
    public static final int OTP_TYPE_PHONE_NUMBER_VERIFICATION = 11;
    public static final int OTP_TYPE_CHANGE_PHONE_NUMBER = 20;
    public static final int OTP_TYPE_LOGIN_PHONE_NUMBER = 112;
    public static final int OTP_TYPE_REGISTER_PHONE_NUMBER = 116;
    public static final int OTP_TYPE_CHECKOUT_DIGITAL = 16;
    public static final int OTP_TYPE_ADD_BANK_ACCOUNT = 12;
    public static final int OTP_TYPE_VERIFY_USER_CHANGE_PHONE_NUMBER = 200;
    public static final int OTP_TYPE_VERIFY_AUTH_CREDIT_CARD = 122;
    public static final int OTP_TYPE_TOKOCASH = -1;

    private static final String TYPE_ANDROID = "1";

    private final OtpSource otpSource;

    @Inject
    public RequestOtpUseCase(OtpSource otpSource) {
        super();
        this.otpSource = otpSource;
    }

    @Override
    public Observable<RequestOtpViewModel> createObservable(RequestParams requestParams) {
        return otpSource.requestOtp(requestParams.getParameters());
    }

    public static RequestParams getParam(String mode, String phone, int otpType, String
            userId) {
        RequestParams param = RequestParams.create();
        param.putString(PARAM_USER_ID, userId);
        param.putString(PARAM_MSISDN, phone);
        param.putString(PARAM_MODE, mode);
        param.putInt(PARAM_OTP_TYPE, otpType);
        param.putString(PARAM_OS_TYPE, TYPE_ANDROID);
        return param;
    }

    public static RequestParams getParamEmail(String email, int otpType,
                                              String userId) {
        RequestParams param = RequestParams.create();
        param.putString(PARAM_USER_ID, userId);
        param.putString(PARAM_EMAIL, email);
        param.putString(PARAM_MODE, MODE_EMAIL);
        param.putInt(PARAM_OTP_TYPE, otpType);
        param.putString(PARAM_OS_TYPE, TYPE_ANDROID);
        return param;
    }


}
