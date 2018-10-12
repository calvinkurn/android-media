package com.tokopedia.phoneverification.domain.mapper;

import android.text.TextUtils;

import com.tokopedia.abstraction.common.network.response.TokopediaWsV4Response;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.otp.common.network.ErrorMessageException;
import com.tokopedia.phoneverification.data.model.ChangePhoneNumberViewModel;
import com.tokopedia.phoneverification.domain.pojo.ChangePhoneNumberPojo;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by nisie on 5/10/17.
 */

public class ChangePhoneNumberMapper implements Func1<Response<TokopediaWsV4Response>, ChangePhoneNumberViewModel> {

    @Inject
    public ChangePhoneNumberMapper() {
    }

    @Override
    public ChangePhoneNumberViewModel call(Response<TokopediaWsV4Response> response) {
        if (response.isSuccessful()) {
            if ((!response.body().isNullData()
                    && response.body().getErrorMessageJoined().equals(""))
                    || (!response.body().isNullData()
                    && response.body().getErrorMessages() == null)) {
                ChangePhoneNumberPojo pojo = response.body().convertDataObj(ChangePhoneNumberPojo
                        .class);
                return mappingToViewModel(pojo, response.body().getStatusMessageJoined());
            } else {
                if (response.body().getErrorMessages() != null
                        && !response.body().getErrorMessages().isEmpty()) {
                    throw new ErrorMessageException(response.body().getErrorMessageJoined());
                } else {
                    throw new ErrorMessageException("");
                }
            }
        } else {
            String messageError = ErrorHandler.getErrorMessage(response);
            if (!TextUtils.isEmpty(messageError)) {
                throw new ErrorMessageException(messageError);
            } else {
                throw new RuntimeException(String.valueOf(response.code()));
            }
        }
    }

    private ChangePhoneNumberViewModel mappingToViewModel(ChangePhoneNumberPojo pojo,
                                                          String statusMessage) {
        return new ChangePhoneNumberViewModel(pojo.getIsSuccess() == 1, statusMessage);
    }
}
