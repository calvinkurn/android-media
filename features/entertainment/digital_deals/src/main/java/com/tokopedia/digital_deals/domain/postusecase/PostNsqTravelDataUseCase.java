package com.tokopedia.digital_deals.domain.postusecase;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.network.data.model.response.DataResponse;
import com.tokopedia.common.network.data.model.RequestType;
import com.tokopedia.common.network.data.model.RestRequest;
import com.tokopedia.common.network.domain.RestRequestUseCase;
import com.tokopedia.digital_deals.data.source.DealsUrl;
import com.tokopedia.digital_deals.view.model.nsqevents.NsqTravelRecentSearchModel;
import com.tokopedia.usecase.RequestParams;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class PostNsqTravelDataUseCase extends RestRequestUseCase {

    private NsqTravelRecentSearchModel nsqTravelRecentSearchModel;

    @Inject
    public PostNsqTravelDataUseCase() {

    }

    public void setTravelDataRequestModel(NsqTravelRecentSearchModel nsqTravelRecentSearchModel) {
        this.nsqTravelRecentSearchModel = nsqTravelRecentSearchModel;
    }

    @Override
    protected List<RestRequest> buildRequest(RequestParams requestParams) {
        List<RestRequest> tempRequest = new ArrayList<>();

        String url = DealsUrl.DEALS_DOMAIN + DealsUrl.HelperUrl.DEALS_NSQ_EVENT;
        Type token = new TypeToken<DataResponse<String>>() {
        }.getType();

        RestRequest restRequest1 = new RestRequest.Builder(url, token)
                .setRequestType(RequestType.POST)
                .setBody(nsqTravelRecentSearchModel)
                .build();
        tempRequest.add(restRequest1);
        return tempRequest;
    }
}
