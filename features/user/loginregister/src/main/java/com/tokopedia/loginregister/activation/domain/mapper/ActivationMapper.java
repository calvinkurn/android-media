package com.tokopedia.loginregister.activation.domain.mapper;

import com.tokopedia.loginregister.activation.domain.pojo.ActivateUnicodePojo;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 10/19/18.
 */
public class ActivationMapper implements Func1<Response<ActivateUnicodePojo>,
        ActivateUnicodePojo> {

    @Inject
    public ActivationMapper() {
    }

    @Override
    public ActivateUnicodePojo call(Response<ActivateUnicodePojo> response) {
        if (response.isSuccessful()) {
            return response.body();
        } else {
            throw new RuntimeException(String.valueOf(response.code()));
        }

    }
}
