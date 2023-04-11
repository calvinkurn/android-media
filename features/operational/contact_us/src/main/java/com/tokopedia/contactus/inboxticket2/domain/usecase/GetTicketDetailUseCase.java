package com.tokopedia.contactus.inboxticket2.domain.usecase;


import com.google.gson.reflect.TypeToken;
import com.tokopedia.common.network.data.model.CacheType;
import com.tokopedia.common.network.data.model.RestCacheStrategy;
import com.tokopedia.common.network.data.model.RestRequest;
import com.tokopedia.common.network.domain.RestRequestUseCase;
import com.tokopedia.contactus.inboxticket2.data.InboxEndpoint;
import com.tokopedia.contactus.inboxticket2.domain.TicketDetailResponse;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.network.data.model.response.DataResponse;
import com.tokopedia.usecase.RequestParams;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class GetTicketDetailUseCase extends RestRequestUseCase {
    private String ticketId;

    @Inject
    GetTicketDetailUseCase() {

    }

    @Override
    protected List<RestRequest> buildRequest(RequestParams requestParams) {
        List<RestRequest> tempRequest = new ArrayList<>();

        RestRequest restRequest1 = new RestRequest.Builder(getUrl(), new TypeToken<DataResponse<TicketDetailResponse>>() {
        }.getType())
                .setQueryParams(null)
                .setCacheStrategy(new RestCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
                .build();
        tempRequest.add(restRequest1);

        return tempRequest;
    }

    public void setTicketId(String id) {
        ticketId = id;
    }

    private String getUrl() {
        return TkpdBaseURL.BASE_CONTACT_US + InboxEndpoint.DETAIL_TICKET + ticketId;
    }
}
