package com.tokopedia.forgotpassword.domain;

import com.tokopedia.forgotpassword.data.pojo.ResetPasswordPojo;
import com.tokopedia.network.data.model.response.DataResponse;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 9/25/18.
 */
public class ResetPasswordMapper implements Func1<Response<DataResponse<ResetPasswordPojo>>,
        ResetPasswordPojo> {

    @Inject
    public ResetPasswordMapper() {
    }

    @Override
    public ResetPasswordPojo call(Response<DataResponse<ResetPasswordPojo>> response) {
        if (response.isSuccessful()) {
            return response.body().getData();

        } else {
            throw new RuntimeException(String.valueOf(response.code()));
        }
    }
}
