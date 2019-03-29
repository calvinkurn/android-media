package com.tokopedia.events.domain.scanTicketUsecase;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.common.network.data.model.RequestType;
import com.tokopedia.common.network.data.model.RestRequest;
import com.tokopedia.common.network.domain.RestRequestUseCase;
import com.tokopedia.events.domain.model.scanticket.ScanTicketResponse;
import com.tokopedia.usecase.RequestParams;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class RedeemTicketUseCase extends RestRequestUseCase {
    private String url;

    @Inject
    public RedeemTicketUseCase() {
    }

    public void setRequestUrl(String url) {
        this.url = url;
    }

    @Override
    protected List<RestRequest> buildRequest(RequestParams requestParams) {
        List<RestRequest> tempRequest = new ArrayList<>();
        Type token = new TypeToken<DataResponse<JsonObject>>() {
        }.getType();

        RestRequest restRequest1 = new RestRequest.Builder(url, token)
                .setRequestType(RequestType.POST)
                .setBody("")
                .build();
        tempRequest.add(restRequest1);
        return tempRequest;
    }
}
