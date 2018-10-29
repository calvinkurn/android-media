package com.tokopedia.phoneverification.domain.interactor;

import com.tokopedia.phoneverification.data.VerifyPhoneNumberDomain;
import com.tokopedia.phoneverification.data.source.VerifyMsisdnSource;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by alvinatin on 25/10/18.
 */

public class VerifyPhoneNumberUseCase extends UseCase<VerifyPhoneNumberDomain>{
    public static final String PARAM_USER_ID = "user_id";
    public static final String PARAM_PHONE = "phone";
    private final VerifyMsisdnSource verifyMsisdnSource;

    @Inject
    public VerifyPhoneNumberUseCase(VerifyMsisdnSource verifyMsisdnSource) {
        this.verifyMsisdnSource = verifyMsisdnSource;
    }



    @Override
    public Observable<VerifyPhoneNumberDomain> createObservable(RequestParams requestParams) {
        return verifyMsisdnSource.verifyPhoneNumber(requestParams.getParameters());
    }

    public static RequestParams getParam(String userId, String phone) {
        RequestParams param = RequestParams.create();
        param.putString(VerifyPhoneNumberUseCase.PARAM_USER_ID, userId);
        param.putString(VerifyPhoneNumberUseCase.PARAM_PHONE, phone);
        return param;
    }
}
