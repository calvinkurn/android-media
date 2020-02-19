package com.tokopedia.digital_deals.domain.getusecase;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.network.data.model.response.DataResponse;
import com.tokopedia.common.network.data.model.RestRequest;
import com.tokopedia.common.network.domain.RestRequestUseCase;
import com.tokopedia.digital_deals.data.source.DealsUrl;
import com.tokopedia.digital_deals.view.model.response.AllBrandsResponse;
import com.tokopedia.usecase.RequestParams;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

public class GetAllBrandsUseCase extends RestRequestUseCase {
    private  RequestParams params;

    @Inject
    public GetAllBrandsUseCase(){ }

    public void setRequestParams(RequestParams requestParams) {
        this.params = requestParams;
    }

    @Override
    protected List<RestRequest> buildRequest(RequestParams requestParams) {
        List<RestRequest> tempRequest = new ArrayList<>();

        HashMap<String, Object> map = params.getParameters();
        String url = DealsUrl.DEALS_DOMAIN + DealsUrl.HelperUrl.DEALS_LIST_SEARCH;
        //Request 1
        Type token = new TypeToken<DataResponse<AllBrandsResponse>>() {
        }.getType();

        RestRequest restRequest1 = new RestRequest.Builder(url, token)
                .setQueryParams(params.getParameters())
                .build();
        tempRequest.add(restRequest1);
        return tempRequest;
    }
}
