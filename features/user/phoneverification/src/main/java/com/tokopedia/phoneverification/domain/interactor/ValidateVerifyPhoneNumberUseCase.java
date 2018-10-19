package com.tokopedia.phoneverification.domain.interactor;

import com.tokopedia.otp.cotp.domain.interactor.ValidateOtpUseCase;
import com.tokopedia.phoneverification.data.VerifyPhoneNumberDomain;
import com.tokopedia.phoneverification.data.model.ValidateVerifyPhoneNumberDomain;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;
import com.tokopedia.user.session.UserSession;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * @author by nisie on 10/24/17.
 */

public class ValidateVerifyPhoneNumberUseCase extends UseCase<ValidateVerifyPhoneNumberDomain> {

//    private ValidateOtpUseCase validateOtpUseCase;
    private VerifyPhoneNumberUseCase verifyPhoneNumberUseCase;
    private UserSession userSession;

    @Inject
    public ValidateVerifyPhoneNumberUseCase(/*ValidateOtpUseCase validateOtpUseCase,*/
                                            VerifyPhoneNumberUseCase verifyPhoneNumberUseCase,
                                            UserSession userSession) {
//        this.validateOtpUseCase = validateOtpUseCase;
        this.verifyPhoneNumberUseCase = verifyPhoneNumberUseCase;
        this.userSession = userSession;
    }

    @Override
    public Observable<ValidateVerifyPhoneNumberDomain> createObservable(RequestParams requestParams) {
        ValidateVerifyPhoneNumberDomain domain = new ValidateVerifyPhoneNumberDomain();
        return validateOtp(requestParams, domain)
                .flatMap(verifyPhoneNumber(requestParams, domain));
    }

    private Func1<ValidateVerifyPhoneNumberDomain, Observable<ValidateVerifyPhoneNumberDomain>>
    verifyPhoneNumber(final RequestParams requestParams, final ValidateVerifyPhoneNumberDomain domain) {
        return new Func1<ValidateVerifyPhoneNumberDomain, Observable<ValidateVerifyPhoneNumberDomain>>() {
            @Override
            public Observable<ValidateVerifyPhoneNumberDomain> call(ValidateVerifyPhoneNumberDomain validateVerifyPhoneNumberDomain) {
                return verifyPhoneNumberUseCase.createObservable(VerifyPhoneNumberUseCase.getParam(
                        requestParams.getString(VerifyPhoneNumberUseCase.PARAM_USER_ID, ""),
                        requestParams.getString(VerifyPhoneNumberUseCase.PARAM_PHONE, "")))
                        .flatMap(new Func1<VerifyPhoneNumberDomain, Observable<ValidateVerifyPhoneNumberDomain>>() {
                            @Override
                            public Observable<ValidateVerifyPhoneNumberDomain> call(VerifyPhoneNumberDomain verifyPhoneNumberDomain) {
                                verifyPhoneNumberDomain.setPhoneNumber(requestParams.getString
                                        (VerifyPhoneNumberUseCase.PARAM_PHONE, ""));
                                domain.setVerifyPhoneDomain(verifyPhoneNumberDomain);
                                return Observable.just(domain);
                            }
                        })
                        .doOnNext(saveToSession());
            }
        };
    }

    private Action1<ValidateVerifyPhoneNumberDomain> saveToSession() {
        return new Action1<ValidateVerifyPhoneNumberDomain>() {
            @Override
            public void call(ValidateVerifyPhoneNumberDomain validateVerifyPhoneNumberDomain) {
                if (validateVerifyPhoneNumberDomain.getVerifyPhoneDomain().isSuccess()) {
                    userSession.setIsMsisdnVerified(true);
                    userSession.setPhoneNumber(validateVerifyPhoneNumberDomain.getVerifyPhoneDomain().getPhoneNumber());
                }
            }
        };
    }


    private Observable<ValidateVerifyPhoneNumberDomain> validateOtp(RequestParams requestParams,
                                                                    final ValidateVerifyPhoneNumberDomain domain) {
//        return validateOtpUseCase.createObservable(
//                ValidateOtpUseCase.getParam(
//                        requestParams.getString(ValidateOtpUseCase.PARAM_USER, ""),
//                        requestParams.getInt(ValidateOtpUseCase.PARAM_OTP_TYPE, -1),
//                        requestParams.getString(ValidateOtpUseCase.PARAM_CODE, "")
//                ))
//                .flatMap(new Func1<ValidateOtpDomain, Observable<ValidateVerifyPhoneNumberDomain>>() {
//                    @Override
//                    public Observable<ValidateVerifyPhoneNumberDomain> call(ValidateOtpDomain validateOTPDomain) {
//                        domain.setValidateOtpDomain(validateOTPDomain);
//                        return Observable.just(domain);
//                    }
//                });
        return Observable.just(null);
    }

    public static RequestParams getParam(int otpType, String otpCode, String phoneNumber, String
            userId) {
        RequestParams params = RequestParams.create();
        params.putAll(ValidateOtpUseCase.getParam(userId, otpType, otpCode).getParameters());
        params.putAll(VerifyPhoneNumberUseCase.getParam(userId, phoneNumber).getParameters());
        return params;
    }
}
