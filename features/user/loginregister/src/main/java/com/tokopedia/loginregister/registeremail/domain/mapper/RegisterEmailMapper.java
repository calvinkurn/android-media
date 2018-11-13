package com.tokopedia.loginregister.registeremail.domain.mapper;

import com.tokopedia.loginregister.registeremail.domain.pojo.RegisterEmailPojo;
import com.tokopedia.network.data.model.response.DataResponse;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 10/25/18.
 */
public class RegisterEmailMapper implements Func1<Response<DataResponse<RegisterEmailPojo>>, RegisterEmailPojo> {

    @Inject
    public RegisterEmailMapper() {
    }

    @Override
    public RegisterEmailPojo call(Response<DataResponse<RegisterEmailPojo>> response) {

        if (response.isSuccessful()) {
            return response.body().getData();
        } else {
            throw new RuntimeException(String.valueOf(response.code()));
        }
    }
}
