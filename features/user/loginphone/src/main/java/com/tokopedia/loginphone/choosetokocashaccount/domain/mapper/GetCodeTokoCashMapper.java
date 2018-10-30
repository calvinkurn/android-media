package com.tokopedia.loginphone.choosetokocashaccount.domain.mapper;

import com.tokopedia.loginphone.choosetokocashaccount.data.GetCodeTokoCashPojo;
import com.tokopedia.network.data.model.response.DataResponse;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 12/5/17.
 */

public class GetCodeTokoCashMapper implements Func1<Response<DataResponse<GetCodeTokoCashPojo>>,
        GetCodeTokoCashPojo> {

    @Inject
    public GetCodeTokoCashMapper() {
    }

    @Override
    public GetCodeTokoCashPojo call(Response<DataResponse<GetCodeTokoCashPojo>> response) {
        if (response.isSuccessful()) {
            return response.body().getData();
        } else {
            throw new RuntimeException(String.valueOf(response.code()));

        }
    }
}
