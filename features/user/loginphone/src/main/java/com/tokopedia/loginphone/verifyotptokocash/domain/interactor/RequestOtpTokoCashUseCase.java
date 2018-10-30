package com.tokopedia.loginphone.verifyotptokocash.domain.interactor;

import com.tokopedia.loginphone.verifyotptokocash.data.source.TokoCashOtpSource;
import com.tokopedia.loginphone.verifyotptokocash.domain.pojo.requestotp.RequestOtpTokoCashPojo;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by nisie on 12/4/17.
 */

public class RequestOtpTokoCashUseCase extends UseCase<RequestOtpTokoCashPojo> {

    private static final String PARAM_PHONE_NUMBER = "msisdn";
    private static final String PARAM_METHOD = "accept";
    public static final String TYPE_SMS = "sms";
    public static final String TYPE_PHONE = "call";

    private TokoCashOtpSource tokoCashLoginSource;

    @Inject
    public RequestOtpTokoCashUseCase(
            TokoCashOtpSource tokoCashLoginSource) {
        this.tokoCashLoginSource = tokoCashLoginSource;
    }

    @Override
    public Observable<RequestOtpTokoCashPojo> createObservable(RequestParams requestParams) {
        return tokoCashLoginSource.requestLoginOtp(requestParams.getParameters());
    }

    public static RequestParams getParam(String phoneNumber, String type) {
        RequestParams params = RequestParams.create();
        params.putString(PARAM_PHONE_NUMBER, phoneNumber);
        params.putString(PARAM_METHOD, type);
        return params;
    }
}
