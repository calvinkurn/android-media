package com.tokopedia.loginphone.checkloginphone.domain.mapper;

import com.tokopedia.loginphone.checkloginphone.domain.pojo.CheckMsisdnTokoCashPojo;
import com.tokopedia.network.data.model.response.DataResponse;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 12/6/17.
 */

public class CheckMsisdnTokoCashMapper implements
        Func1<Response<DataResponse<CheckMsisdnTokoCashPojo>>, CheckMsisdnTokoCashPojo> {

    @Inject
    public CheckMsisdnTokoCashMapper() {
    }

    @Override
    public CheckMsisdnTokoCashPojo call(Response<DataResponse<CheckMsisdnTokoCashPojo>> response) {
        if (response.isSuccessful() && response.body() != null) {
            return response.body().getData();
        } else {
            throw new RuntimeException(String.valueOf(response.code()));
        }
    }
}
