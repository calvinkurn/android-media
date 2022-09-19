package com.tokopedia.contactus.inboxticket2.domain.usecase;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.common.network.data.model.CacheType;
import com.tokopedia.common.network.data.model.RequestType;
import com.tokopedia.common.network.data.model.RestCacheStrategy;
import com.tokopedia.common.network.data.model.RestRequest;
import com.tokopedia.common.network.domain.RestRequestUseCase;
import com.tokopedia.contactus.inboxticket2.data.InboxEndpoint;
import com.tokopedia.contactus.inboxticket2.domain.InboxDataResponse;
import com.tokopedia.contactus.inboxticket2.domain.RatingResponse;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.usecase.RequestParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;


public class PostRatingUseCase extends RestRequestUseCase {

    private Map<String, Object> queryMap;

    @Inject
    PostRatingUseCase() {
        queryMap = new HashMap<>();
    }

    @Override
    protected List<RestRequest> buildRequest(RequestParams requestParams) {
        List<RestRequest> tempRequest = new ArrayList<>();
        RestRequest restRequest1 = new RestRequest.Builder(getUrl(), new TypeToken<InboxDataResponse<RatingResponse>>() {
        }.getType())
                .setRequestType(RequestType.POST)
                .setBody(queryMap)
                .setCacheStrategy(new RestCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
                .build();
        tempRequest.add(restRequest1);

        return tempRequest;
    }

    private String getUrl() {
        return TkpdBaseURL.BASE_CONTACT_US + InboxEndpoint.COMMENT_RATING;
    }

    public void setQueryMap(String commentID, String rating, int badreason, int badreasoncode, String customReason) {
        queryMap.clear();
        queryMap.put("comment_id", commentID);
        queryMap.put("rating", rating);
        if (badreason == 1)
            queryMap.put("show_bad_reason", 1);
        if (badreasoncode > 0)
            queryMap.put("bad_reason_code", badreasoncode);
        if (badreasoncode == 7)
            queryMap.put("bad_reason", customReason);
    }
}
