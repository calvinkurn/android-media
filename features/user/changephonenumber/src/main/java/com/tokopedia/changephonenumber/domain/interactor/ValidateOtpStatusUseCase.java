package com.tokopedia.changephonenumber.domain.interactor;

import com.tokopedia.changephonenumber.domain.ChangePhoneNumberRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by alvinatin on 11/05/18.
 */

public class ValidateOtpStatusUseCase extends UseCase<Boolean> {

    private static final String PARAM_USER_ID = "user_id";
    private static final String PARAM_OTP_TYPE = "otp_type";

    private final ChangePhoneNumberRepository changePhoneNumberRepository;

    @Inject
    public ValidateOtpStatusUseCase(ChangePhoneNumberRepository changePhoneNumberRepository) {
        this.changePhoneNumberRepository = changePhoneNumberRepository;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return changePhoneNumberRepository.validateOtpStatus(requestParams.getParameters());
    }

    public static RequestParams getValidateOtpParam(int userId, int otpType){
        RequestParams param = RequestParams.create();
        param.putInt(PARAM_USER_ID, userId);
        param.putInt(PARAM_OTP_TYPE, otpType);
        return param;
    }
}
