package com.tokopedia.topads.dashboard.data.mapper;

import com.tokopedia.topads.dashboard.data.model.data.GroupAd;
import com.tokopedia.seller.common.data.response.DataResponse;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 2/20/17.
 */
public class TopAdsSearchGroupMapper implements Func1<Response<DataResponse<List<GroupAd>>>, List<GroupAd>> {

    @Inject
    public TopAdsSearchGroupMapper() {
    }

    @Override
    public List<GroupAd> call(Response<DataResponse<List<GroupAd>>> dataResponseResponse) {
        return mappingResponse(dataResponseResponse);
    }

    private List<GroupAd> mappingResponse(Response<DataResponse<List<GroupAd>>> response) {
        if (response.isSuccessful() && response.body() != null
                && response.body().getData() != null) {
            return response.body().getData();
        } else {
            return Collections.emptyList();
        }
    }
}
