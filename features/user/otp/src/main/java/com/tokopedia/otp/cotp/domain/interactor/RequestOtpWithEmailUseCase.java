package com.tokopedia.otp.cotp.domain.interactor;


import com.tokopedia.otp.cotp.domain.source.OtpSource;
import com.tokopedia.otp.cotp.view.viewmodel.RequestOtpViewModel;
import com.tokopedia.usecase.RequestParams;

import java.util.Date;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by nisie on 1/2/18.
 */

public class RequestOtpWithEmailUseCase extends RequestOtpUseCase {

    private static final String PARAM_USER = "user";
    private static final String PARAM_TYPE = "type";

    private static final String PARAM_OS_TYPE = "os_type";
    private static final String TYPE_ANDROID = "1";
    private final OtpSource otpSource;

    @Inject
    public RequestOtpWithEmailUseCase(OtpSource otpSource) {
        super(otpSource);
        this.otpSource = otpSource;
    }

    @Override
    public Observable<RequestOtpViewModel> createObservable(RequestParams requestParams) {
        return otpSource.requestOtpWithEmail(requestParams.getParameters());
    }

    public static RequestParams getParam(String email, int otpType,
                                         String tempUserId) {
        RequestParams param = RequestParams.create();
        param.putString(PARAM_USER, tempUserId);
        param.putString(PARAM_EMAIL, email);
        param.putInt(PARAM_TYPE, otpType);
        param.putString(PARAM_OS_TYPE, TYPE_ANDROID);
        return param;
    }
}
