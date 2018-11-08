package com.tokopedia.changephonenumber.domain.interactor;

import com.tokopedia.changephonenumber.domain.ChangePhoneNumberRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by milhamj on 03/01/18.
 */

public class ValidateNumberUseCase extends UseCase<Boolean> {
    private static final String PARAM_OS_TYPE = "theme";
    private static final String OS_TYPE_ANDROID = "mobile";
    private static final String PARAM_ACTION = "action";
    private static final String PARAM_NEW_MSISDN = "new_msisdn";
    private static final String ACTION_VALIDATE = "validate";
    private static final String ACTION_SUMBIT = "submit";

    private final ChangePhoneNumberRepository changePhoneNumberRepository;

    @Inject
    public ValidateNumberUseCase(ChangePhoneNumberRepository changePhoneNumberRepository) {
        this.changePhoneNumberRepository = changePhoneNumberRepository;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return changePhoneNumberRepository.validateNumber(requestParams.getParameters());
    }

    public static RequestParams getValidateNumberParam(String newPhoneNumber) {
        RequestParams param = RequestParams.create();
        param.putString(PARAM_ACTION, ACTION_VALIDATE);
        param.putString(PARAM_NEW_MSISDN, newPhoneNumber);
        param.putString(PARAM_OS_TYPE, OS_TYPE_ANDROID);
        return param;
    }

    public static RequestParams getSubmitNumberParam(String newPhoneNumber) {
        RequestParams param = RequestParams.create();
        param.putString(PARAM_ACTION, ACTION_SUMBIT);
        param.putString(PARAM_NEW_MSISDN, newPhoneNumber);
        param.putString(PARAM_OS_TYPE, OS_TYPE_ANDROID);
        return param;
    }
}
