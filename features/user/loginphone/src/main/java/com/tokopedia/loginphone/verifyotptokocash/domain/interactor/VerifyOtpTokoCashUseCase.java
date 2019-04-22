package com.tokopedia.loginphone.verifyotptokocash.domain.interactor;

import com.tokopedia.loginphone.verifyotptokocash.data.source.TokoCashOtpSource;
import com.tokopedia.loginphone.verifyotptokocash.domain.pojo.verifyotp.VerifyOtpTokoCashPojo;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by nisie on 12/4/17.
 */
@Deprecated
public class VerifyOtpTokoCashUseCase extends UseCase<VerifyOtpTokoCashPojo> {

    private static final String PARAM_PHONE_NUMBER = "msisdn";
    private static final String PARAM_OTP_CODE = "otp";

    private final TokoCashOtpSource tokoCashLoginSource;

    @Inject
    public VerifyOtpTokoCashUseCase(
            TokoCashOtpSource tokoCashLoginSource) {
        this.tokoCashLoginSource = tokoCashLoginSource;
    }

    public static RequestParams getParam(String phoneNumber, String otpCode) {
        RequestParams params = RequestParams.create();
        params.putString(PARAM_PHONE_NUMBER, phoneNumber);
        params.putString(PARAM_OTP_CODE, otpCode);
        return params;
    }

    @Override
    public Observable<VerifyOtpTokoCashPojo> createObservable(RequestParams requestParams) {
        return tokoCashLoginSource.verifyOtpTokoCash(requestParams.getParameters());
    }
}
