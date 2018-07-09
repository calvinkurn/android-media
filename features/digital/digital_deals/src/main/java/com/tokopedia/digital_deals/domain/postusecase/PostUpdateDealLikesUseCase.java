package com.tokopedia.digital_deals.domain.postusecase;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.common.network.data.model.RequestType;
import com.tokopedia.common.network.data.model.RestRequest;
import com.tokopedia.common.network.domain.RestRequestUseCase;
import com.tokopedia.digital_deals.data.source.DealsBaseURL;
import com.tokopedia.digital_deals.data.source.DealsUrl;
import com.tokopedia.digital_deals.domain.model.LikeUpdateResultDomain;
import com.tokopedia.digital_deals.domain.model.request.likes.LikeUpdateModel;
import com.tokopedia.usecase.RequestParams;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


public class PostUpdateDealLikesUseCase extends RestRequestUseCase {

    private RequestParams params;

    @Inject
    public PostUpdateDealLikesUseCase() {
    }

    public void setRequestParams(RequestParams params) {
        this.params = params;
    }

    @Override
    protected List<RestRequest> buildRequest() {
        List<RestRequest> tempRequest = new ArrayList<>();

        LikeUpdateModel requestModel = (LikeUpdateModel) params.getObject("request_body");
        String url = DealsBaseURL.DEALS_DOMAIN + DealsUrl.DEALS_LIKES;
        Type token = new TypeToken<DataResponse<LikeUpdateResultDomain>>() {
        }.getType();

        RestRequest restRequest1 = new RestRequest.Builder(url, token)
                .setRequestType(RequestType.POST)
                .setBody(requestModel)
                .build();
        tempRequest.add(restRequest1);
        return tempRequest;
    }
}
