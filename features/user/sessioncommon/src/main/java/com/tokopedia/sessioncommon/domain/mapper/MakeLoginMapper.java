package com.tokopedia.sessioncommon.domain.mapper;

import com.tokopedia.network.data.model.response.DataResponse;
import com.tokopedia.sessioncommon.data.model.MakeLoginPojo;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 5/26/17.
 */

public class MakeLoginMapper implements Func1<Response<DataResponse<MakeLoginPojo>>, MakeLoginPojo> {

    @Inject
    public MakeLoginMapper() {
    }

    @Override
    public MakeLoginPojo call(Response<DataResponse<MakeLoginPojo>> response) {
        if (response.isSuccessful()) {
            return response.body().getData();
        } else {
            throw new RuntimeException(String.valueOf(response.code()));
        }
    }

}
