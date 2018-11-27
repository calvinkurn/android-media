package com.tokopedia.events.domain.scanTicketUsecase;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.common.network.data.model.RequestType;
import com.tokopedia.common.network.data.model.RestRequest;
import com.tokopedia.common.network.domain.RestRequestUseCase;
import com.tokopedia.events.data.source.EventsUrl;
import com.tokopedia.events.domain.model.scanticket.CheckScanOption;
import com.tokopedia.usecase.RequestParams;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class CheckScanOptionUseCase extends RestRequestUseCase {
    private RequestParams params;

    @Inject
    public CheckScanOptionUseCase(){ }

    public void setRequestParams(RequestParams requestParams) {
        this.params = requestParams;
    }

    @Override
    protected List<RestRequest> buildRequest(RequestParams requestParams) {
        List<RestRequest> tempRequest = new ArrayList<>();

        String url = EventsUrl.EVENTS_DOMAIN + EventsUrl.EVENT_SCAN_TICKET_URL;
        //Request 1
        Type token = new TypeToken<DataResponse<CheckScanOption>>() {
        }.getType();

        RestRequest restRequest1 = new RestRequest.Builder(url, token)
                .setBody(new Gson().toJson(params.getParameters()))
                .setRequestType(RequestType.POST)
                .build();
        tempRequest.add(restRequest1);
        return tempRequest;
    }
}
