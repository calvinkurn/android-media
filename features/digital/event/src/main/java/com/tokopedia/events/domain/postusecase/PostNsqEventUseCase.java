package com.tokopedia.events.domain.postusecase;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.common.network.data.model.RequestType;
import com.tokopedia.common.network.data.model.RestRequest;
import com.tokopedia.common.network.domain.RestRequestUseCase;
import com.tokopedia.events.data.source.EventsUrl;
import com.tokopedia.events.domain.model.NsqServiceModel;
import com.tokopedia.events.domain.model.NsqTravelRecentSearchModel;
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

        String url = EventsUrl.EVENTS_DOMAIN + EventsUrl.NSQ_EVENT_URL;
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
