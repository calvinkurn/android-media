package com.tokopedia.sessioncommon.domain.mapper;

import com.tokopedia.network.data.model.response.DataResponse;
import com.tokopedia.sessioncommon.data.model.GetUserInfoData;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 6/19/17.
 */

public class GetUserInfoMapper implements Func1<Response<GetUserInfoData>,
        GetUserInfoData> {

    private static final String ERROR = "error";
    private static final String ERROR_DESCRIPTION = "error_description";

    @Inject
    public GetUserInfoMapper() {
    }

    @Override
    public GetUserInfoData call(Response<GetUserInfoData> response) {
        if (response.isSuccessful()) {
            return response.body();
        } else {
            throw new RuntimeException(String.valueOf(response.code()));
        }
    }
}
