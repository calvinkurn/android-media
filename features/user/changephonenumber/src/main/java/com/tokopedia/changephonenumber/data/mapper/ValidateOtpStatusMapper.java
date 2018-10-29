package com.tokopedia.changephonenumber.data.mapper;

import android.text.TextUtils;

import com.tokopedia.abstraction.common.network.exception.MessageErrorException;
import com.tokopedia.changephonenumber.data.model.ValidateOtpStatusData;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * @author by alvinatin on 11/05/18.
 */

public class ValidateOtpStatusMapper implements Func1<Response<ValidateOtpStatusData>, Boolean> {

    @Inject
    public ValidateOtpStatusMapper() {
    }

    @Override
    public Boolean call(Response<ValidateOtpStatusData> response) {
        boolean isValid = false;
        if (response.isSuccessful()) {
            if (response.body() != null) {
                if (TextUtils.isEmpty(response.body().getHeader().getErrorCode())) {
                    isValid = response.body().getData().getValidate();
                } else {
                    if (response.body().getHeader().getMessages() != null) {
                        throw new RuntimeException(response.body().getHeader().getMessages().get
                                (0));
                    } else {
                        throw new RuntimeException("Error with no message");
                    }
                }
            } else {
                Observable.error(new MessageErrorException(response.errorBody().toString()));
            }

        } else {
            throw new RuntimeException("Response Error");
        }
        return isValid;
    }
}
