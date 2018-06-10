package com.tokopedia.topads.dashboard.data.mapper;

import com.tokopedia.topads.dashboard.data.model.response.DataResponseCreateGroup;
import com.tokopedia.seller.common.data.response.DataResponse;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 2/28/17.
 */

public class TopAdsDetailGroupMapper implements Func1<Response<DataResponse<DataResponseCreateGroup>>, DataResponseCreateGroup> {

    @Inject
    public TopAdsDetailGroupMapper() {
    }

    @Override
    public DataResponseCreateGroup call(Response<DataResponse<DataResponseCreateGroup>> dataResponseResponse) {
        return mappingResponse(dataResponseResponse);
    }

    private DataResponseCreateGroup mappingResponse(Response<DataResponse<DataResponseCreateGroup>> dataResponseResponse) {
        if (dataResponseResponse.isSuccessful() && dataResponseResponse.body() != null
                && dataResponseResponse.body().getData() != null) {
            return dataResponseResponse.body().getData();
        } else {
            return null;
        }
    }
}
