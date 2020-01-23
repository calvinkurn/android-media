package com.tokopedia.digital_deals.domain.postusecase;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.network.data.model.response.DataResponse;
import com.tokopedia.common.network.data.model.RequestType;
import com.tokopedia.common.network.data.model.RestRequest;
import com.tokopedia.common.network.domain.RestRequestUseCase;
import com.tokopedia.digital_deals.data.source.DealsUrl;
import com.tokopedia.digital_deals.view.model.nsqevents.NsqServiceModel;
import com.tokopedia.usecase.RequestParams;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class PostNsqEventUseCase extends RestRequestUseCase {

    private NsqServiceModel nsqServiceModel;

    @Inject
    public PostNsqEventUseCase() {

    }

    public void setRequestModel(NsqServiceModel nsqServiceModel) {
        this.nsqServiceModel = nsqServiceModel;
    }

    @Override
    protected List<RestRequest> buildRequest(RequestParams requestParams) {
        List<RestRequest> tempRequest = new ArrayList<>();

        String url = DealsUrl.DEALS_DOMAIN + DealsUrl.HelperUrl.DEALS_NSQ_EVENT;
        Type token = new TypeToken<DataResponse<String>>() {
        }.getType();

        RestRequest restRequest = new RestRequest.Builder(url, token)
                .setRequestType(RequestType.POST)
                .setBody(nsqServiceModel)
                .build();
        tempRequest.add(restRequest);
        return tempRequest;
    }
}
