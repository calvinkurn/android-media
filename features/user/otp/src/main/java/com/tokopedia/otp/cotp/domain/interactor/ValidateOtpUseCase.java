package com.tokopedia.otp.cotp.domain.interactor;


import com.tokopedia.otp.cotp.domain.source.OtpSource;
import com.tokopedia.otp.cotp.view.viewmodel.ValidateOtpDomain;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by nisie on 10/21/17.
 */

public class ValidateOtpUseCase extends UseCase<ValidateOtpDomain> {
    public static final String PARAM_OTP_TYPE = "otp_type";
    public static final String PARAM_USER = "user";
    public static final String PARAM_CODE = "code";
    public static final String PARAM_PHONE = "msisdn";
    public static final String PARAM_MODE = "mode";

    public static final String MODE_SMS = "sms";
    public static final String MODE_PIN = "PIN";
    public static final String MODE_CALL = "call";
    public static final String MODE_EMAIL = "email";

    public static final int OTP_TYPE_SECURITY_QUESTION = 13;
    public static final int OTP_TYPE_PHONE_NUMBER_VERIFICATION = 11;
    public static final int OTP_TYPE_CHANGE_PHONE_NUMBER = 20;

    private final OtpSource otpSource;

    @Inject
    public ValidateOtpUseCase(OtpSource otpSource) {
        super();
        this.otpSource = otpSource;
    }

    @Override
    public Observable<ValidateOtpDomain> createObservable(RequestParams requestParams) {
        return otpSource.validateOtp(requestParams.getParameters());
    }

    public static RequestParams getParam(String userId, int otpType, String otp,
                                         String phoneNumber, String mode) {
        RequestParams param = RequestParams.create();
        param.putString(PARAM_USER, userId);
        param.putInt(PARAM_OTP_TYPE, otpType);
        param.putString(PARAM_MODE, mode);
        param.putString(PARAM_CODE, otp);
        if(!phoneNumber.isEmpty()) {
            param.putString(PARAM_PHONE, phoneNumber);
        }

        return param;
    }

    public static RequestParams getRegisterPhoneNumberParam(String phoneNumber, int otpType, String otp, String mode) {
        RequestParams param = RequestParams.create();
        param.putString(PARAM_PHONE, phoneNumber);
        param.putInt(PARAM_OTP_TYPE, otpType);
        param.putString(PARAM_CODE, otp);
        param.putString(PARAM_MODE, mode);
        return param;
    }
}
