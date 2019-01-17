package com.tokopedia.contactus.inboxticket2.domain.usecase;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.common.network.data.model.CacheType;
import com.tokopedia.common.network.data.model.RestCacheStrategy;
import com.tokopedia.common.network.data.model.RestRequest;
import com.tokopedia.common.network.domain.RestRequestUseCase;
import com.tokopedia.contactus.inboxticket2.data.InboxEndpoint;
import com.tokopedia.contactus.inboxticket2.domain.TicketListResponse;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.usecase.RequestParams;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class GetTicketListUseCase extends RestRequestUseCase {

    private Map<String, Object> queryMap;
    private String mUrl;

    @Inject
    GetTicketListUseCase() {

    }

    @Override
    protected List<RestRequest> buildRequest(RequestParams requestParams) {
        List<RestRequest> tempRequest = new ArrayList<>();

        RestRequest restRequest1 = new RestRequest.Builder(getUrl(), new TypeToken<DataResponse<TicketListResponse>>() {
        }.getType())
                .setQueryParams(queryMap)
                .setCacheStrategy(new RestCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
                .build();
        tempRequest.add(restRequest1);

        return tempRequest;
    }

    public void setQueryMap(int status, int read, int rating) {
        String STATUS = "status";
        String READ = "read";
        String RATING = "rating";
        if (queryMap == null)
            queryMap = new LinkedHashMap<>();
        queryMap.clear();
        if (status > 0)
            queryMap.put(STATUS, status);
        if (read > 0)
            queryMap.put(READ, read);
        if (rating > 0)
            queryMap.put(RATING, rating);
    }

    public void setUrl(String Url) {
        mUrl = Url;
    }

    private String getUrl() {
        if (mUrl != null && !mUrl.isEmpty())
            return mUrl;
        else
            return TkpdBaseURL.BASE_CONTACT_US + InboxEndpoint.LIST_TICKET;
    }
}
