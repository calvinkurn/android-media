package com.tokopedia.contactus.inboxticket2.domain.usecase;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.common.network.data.model.CacheType;
import com.tokopedia.common.network.data.model.RequestType;
import com.tokopedia.common.network.data.model.RestCacheStrategy;
import com.tokopedia.common.network.data.model.RestRequest;
import com.tokopedia.common.network.domain.RestRequestUseCase;
import com.tokopedia.contactus.inboxticket2.data.InboxEndpoint;
import com.tokopedia.contactus.inboxticket2.domain.InboxDataResponse;
import com.tokopedia.contactus.inboxticket2.domain.StepTwoResponse;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.usecase.RequestParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;


public class PostMessageUseCase2 extends RestRequestUseCase {

    private Map<String, Object> queryMap;

    @Inject
    PostMessageUseCase2() {
        queryMap = new HashMap<>();
    }

    @Override
    protected List<RestRequest> buildRequest(RequestParams requestParams) {
        List<RestRequest> tempRequest = new ArrayList<>();
        RestRequest restRequest1 = new RestRequest.Builder(getUrl(), new TypeToken<InboxDataResponse<StepTwoResponse>>() {
        }.getType())
                .setRequestType(RequestType.POST)
                .setBody(queryMap)
                .setCacheStrategy(new RestCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
                .build();
        tempRequest.add(restRequest1);

        return tempRequest;
    }

    private String getUrl() {
        return TkpdBaseURL.BASE_CONTACT_US + InboxEndpoint.SEND_MESSAGE_ATTACHMENT;
    }

    public void setQueryMap(String fileUploaded, String postKey) {
        queryMap.clear();
        queryMap.put("file_uploaded", fileUploaded);
        queryMap.put("post_key", postKey);
    }
}
