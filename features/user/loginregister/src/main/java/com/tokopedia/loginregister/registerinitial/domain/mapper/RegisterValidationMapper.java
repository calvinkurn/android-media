package com.tokopedia.loginregister.registerinitial.domain.mapper;

import com.tokopedia.loginregister.registerinitial.domain.pojo.RegisterValidationPojo;
import com.tokopedia.network.data.model.response.DataResponse;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 10/25/18.
 */
public class RegisterValidationMapper implements Func1<Response<DataResponse<RegisterValidationPojo>>, RegisterValidationPojo> {

    @Inject
    public RegisterValidationMapper() {
    }

    @Override
    public RegisterValidationPojo call(Response<DataResponse<RegisterValidationPojo>> response) {
        if (response.isSuccessful() && response.body() != null) {
            return response.body().getData();
        } else {
            throw new RuntimeException(String.valueOf(response.code()));
        }
    }
}
