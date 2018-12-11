package com.tokopedia.otp.cotp.domain.source;


import com.tokopedia.otp.cotp.data.CotpApi;
import com.tokopedia.otp.cotp.di.CotpScope;
import com.tokopedia.otp.cotp.domain.interactor.RequestOtpUseCase;
import com.tokopedia.otp.cotp.domain.mapper.RequestOtpMapper;
import com.tokopedia.otp.cotp.domain.mapper.ValidateOtpMapper;
import com.tokopedia.otp.cotp.view.viewmodel.RequestOtpViewModel;
import com.tokopedia.otp.cotp.view.viewmodel.ValidateOtpDomain;
import com.tokopedia.user.session.UserSessionInterface;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Action1;

/**
 * @author by nisie on 10/21/17.
 */

public class OtpSource {

    private static final java.lang.String DATE_FORMAT = "EEE, dd MMM yyyy hh:mm:ss Z";
    private final ValidateOtpMapper validateOtpMapper;
    private final CotpApi otpApi;

    private final RequestOtpMapper requestOTPMapper;

    @Inject
    UserSessionInterface userSession;

    @Inject
    public OtpSource(@CotpScope CotpApi otpApi,
                     RequestOtpMapper
                             requestOTPMapper,
                     ValidateOtpMapper validateOtpMapper) {
        this.otpApi = otpApi;
        this.requestOTPMapper = requestOTPMapper;
        this.validateOtpMapper = validateOtpMapper;
    }

    public Observable<RequestOtpViewModel> requestOtp(HashMap<String, Object> parameters) {
        return otpApi
                .requestOtp(
                        new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH).format(new Date()),
                        parameters.get(RequestOtpUseCase.PARAM_USER_ID).toString(),
                        parameters)
                .map(requestOTPMapper);
    }

    public Observable<ValidateOtpDomain> validateOtp(HashMap<String, Object> parameters) {
        return otpApi
                .validateOtp(parameters)
                .map(validateOtpMapper)
                .doOnNext(saveUuid());
    }

    private Action1<ValidateOtpDomain> saveUuid() {
        return new Action1<ValidateOtpDomain>() {
            @Override
            public void call(ValidateOtpDomain validateOtpData) {
                userSession.setUUID(validateOtpData.getUuid());
            }
        };
    }

    public Observable<RequestOtpViewModel> requestOtpWithEmail(HashMap<String, Object> parameters) {
        return otpApi
                .requestOtpEmail(
                        new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH).format(new Date()),
                        parameters.get(RequestOtpUseCase.PARAM_USER_ID).toString(),
                        parameters)
                .map(requestOTPMapper);
    }
}
