package com.tokopedia.contactus.inboxticket2.domain.usecase;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.common.network.data.model.CacheType;
import com.tokopedia.common.network.data.model.RequestType;
import com.tokopedia.common.network.data.model.RestCacheStrategy;
import com.tokopedia.common.network.data.model.RestRequest;
import com.tokopedia.common.network.domain.RestRequestUseCase;
import com.tokopedia.contactus.inboxticket2.data.InboxEndpoint;
import com.tokopedia.contactus.inboxticket2.domain.TicketListResponse;
import com.tokopedia.core.network.constants.TkpdBaseURL;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * Created by pranaymohapatra on 06/07/18.
 */

public class PostMessageUseCase extends RestRequestUseCase {

    private Map<String, Object> queryMap;

    @Inject
    public PostMessageUseCase() {

    }

    @Override
    protected List<RestRequest> buildRequest() {
        List<RestRequest> tempRequest = new ArrayList<>();
        RestRequest restRequest1 = new RestRequest.Builder(getUrl(), new TypeToken<DataResponse<TicketListResponse>>() {
        }.getType())
                .setRequestType(RequestType.POST)
                .setBody(queryMap)
                .setCacheStrategy(new RestCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
                .build();
        tempRequest.add(restRequest1);

        return tempRequest;
    }

    private String getUrl() {
        return TkpdBaseURL.BASE_CONTACT_US + InboxEndpoint.SEND_MESSAGE;
    }

    public void setQueryMap(int id, String message, int photo, String photoall) {
        queryMap.put("ticket_id", id);
        queryMap.put("message", message);
        if (photo == 1)
            queryMap.put("p_photo", 1);
        if (photoall.length() > 0)
            queryMap.put("p_photo_all", photoall);
    }
}
