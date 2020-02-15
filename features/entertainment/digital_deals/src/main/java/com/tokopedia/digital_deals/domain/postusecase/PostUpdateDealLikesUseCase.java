package com.tokopedia.digital_deals.domain.postusecase;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.network.data.model.response.DataResponse;
import com.tokopedia.common.network.data.model.RequestType;
import com.tokopedia.common.network.data.model.RestRequest;
import com.tokopedia.common.network.domain.RestRequestUseCase;
import com.tokopedia.digital_deals.data.source.DealsUrl;
import com.tokopedia.digital_deals.view.model.response.LikeUpdateResult;
import com.tokopedia.digital_deals.view.model.response.LikeUpdateModel;
import com.tokopedia.usecase.RequestParams;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


public class PostUpdateDealLikesUseCase extends RestRequestUseCase {

    private RequestParams params;
    public static final String REQUEST_BODY="request_body";

    @Inject
    public PostUpdateDealLikesUseCase() {
    }

    public void setRequestParams(RequestParams params) {
        this.params = params;
    }

    @Override
    protected List<RestRequest> buildRequest(RequestParams requestParams) {
        List<RestRequest> tempRequest = new ArrayList<>();

        LikeUpdateModel requestModel = (LikeUpdateModel) params.getObject(PostUpdateDealLikesUseCase.REQUEST_BODY);
        String url = DealsUrl.DEALS_DOMAIN + DealsUrl.HelperUrl.DEALS_LIKES;
        Type token = new TypeToken<DataResponse<LikeUpdateResult>>() {
        }.getType();

        RestRequest restRequest1 = new RestRequest.Builder(url, token)
                .setRequestType(RequestType.POST)
                .setBody(requestModel)
                .build();
        tempRequest.add(restRequest1);
        return tempRequest;
    }
}
