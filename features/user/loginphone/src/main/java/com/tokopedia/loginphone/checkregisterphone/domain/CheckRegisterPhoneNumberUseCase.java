package com.tokopedia.loginphone.checkregisterphone.domain;

import com.tokopedia.loginphone.checkregisterphone.data.CheckMsisdnSource;
import com.tokopedia.loginphone.checkregisterphone.domain.pojo.CheckRegisterPhoneNumberPojo;

import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by yfsx on 28/02/18.
 */

public class CheckRegisterPhoneNumberUseCase extends UseCase<CheckRegisterPhoneNumberPojo> {

    private static final String PARAMS_PHONE_NUMBER = "phone";

    private CheckMsisdnSource checkMsisdnSource;

    @Inject
    public CheckRegisterPhoneNumberUseCase(CheckMsisdnSource checkMsisdnSource) {
        this.checkMsisdnSource = checkMsisdnSource;
    }

    @Override
    public Observable<CheckRegisterPhoneNumberPojo> createObservable(RequestParams requestParams) {
        return checkMsisdnSource.changePhoneNumber(requestParams.getParameters());
    }

    public static RequestParams getParams(String phoneNumber) {
        RequestParams params = RequestParams.create();
        params.putString(PARAMS_PHONE_NUMBER, phoneNumber);
        return params;
    }
}
