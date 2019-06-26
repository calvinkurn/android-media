package com.tokopedia.otp.cotp.domain.interactor;

import com.tokopedia.otp.cotp.view.viewmodel.OtpLoginDomain;
import com.tokopedia.otp.cotp.view.viewmodel.ValidateOtpDomain;
import com.tokopedia.otp.cotp.view.viewmodel.ValidateOtpLoginDomain;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;
import com.tokopedia.user.session.UserSessionInterface;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author by nisie on 10/23/17.
 */

public class ValidateOtpLoginUseCase extends UseCase<ValidateOtpLoginDomain> {

    private UserSessionInterface userSession;
    private ValidateOtpUseCase validateOtpUseCase;
    private MakeLoginUseCase makeLoginUseCase;

    @Inject
    public ValidateOtpLoginUseCase(UserSessionInterface userSession,
                                   ValidateOtpUseCase validateOtpUseCase,
                                   MakeLoginUseCase makeLoginUseCase) {
        super();
        this.userSession = userSession;
        this.validateOtpUseCase = validateOtpUseCase;
        this.makeLoginUseCase = makeLoginUseCase;
    }

    @Override
    public Observable<ValidateOtpLoginDomain> createObservable(RequestParams requestParams) {
        ValidateOtpLoginDomain domain = new ValidateOtpLoginDomain();
        return validateOTP(requestParams, domain)
                .flatMap(makeLogin(requestParams, domain));
    }

    private Observable<ValidateOtpLoginDomain> validateOTP(RequestParams requestParams,
                                                           final ValidateOtpLoginDomain domain) {
        return validateOtpUseCase.createObservable(ValidateOtpUseCase.getParam(
                requestParams.getString(ValidateOtpUseCase.PARAM_USER, ""),
                requestParams.getInt(ValidateOtpUseCase.PARAM_OTP_TYPE, -1),
                requestParams.getString(ValidateOtpUseCase.PARAM_CODE, ""),
                requestParams.getString(ValidateOtpUseCase.PARAM_PHONE, "")
        )).flatMap((Func1<ValidateOtpDomain, Observable<ValidateOtpLoginDomain>>) validateOTPDomain -> {
            if (validateOTPDomain.isSuccess())
                domain.setValidateOtpDomain(validateOTPDomain);
            return Observable.just(domain);
        });
    }

    private Func1<ValidateOtpLoginDomain, Observable<ValidateOtpLoginDomain>> makeLogin(final RequestParams requestParams,
                                                                                        final ValidateOtpLoginDomain domain) {
        return validateOTPLoginDomain -> makeLoginUseCase.createObservable(MakeLoginUseCase.getParam(
                requestParams.getString(MakeLoginUseCase.PARAM_USER_ID, ""),
                userSession.getDeviceId()))
                .flatMap((Func1<OtpLoginDomain, Observable<ValidateOtpLoginDomain>>) makeLoginDomain -> {
                    domain.setMakeLoginDomain(makeLoginDomain);
                    return Observable.just(domain);
                });
    }

    public static RequestParams getParam(int otpType, String otp, String tempUserId,
                                         String deviceId, String phoneNumber) {
        RequestParams params = RequestParams.create();
        params.putAll(ValidateOtpUseCase.getParam(tempUserId, otpType, otp, phoneNumber).getParameters());
        params.putAll(MakeLoginUseCase.getParam(tempUserId, deviceId).getParameters());
        return params;
    }
}
