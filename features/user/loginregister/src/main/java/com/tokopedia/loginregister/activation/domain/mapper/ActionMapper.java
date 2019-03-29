package com.tokopedia.loginregister.activation.domain.mapper;

import com.tokopedia.loginregister.activation.domain.pojo.ActionPojo;
import com.tokopedia.network.data.model.response.DataResponse;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 10/19/18.
 */
public class ActionMapper implements Func1<Response<DataResponse<ActionPojo>>, ActionPojo> {

    @Inject
    public ActionMapper() {
    }

    @Override
    public ActionPojo call(Response<DataResponse<ActionPojo>> response) {
        if (response.isSuccessful()) {
            return response.body().getData();
        } else {
            throw new RuntimeException(String.valueOf(response.code()));
        }
    }
}
