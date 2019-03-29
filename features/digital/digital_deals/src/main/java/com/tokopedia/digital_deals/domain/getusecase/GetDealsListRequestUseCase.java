package com.tokopedia.digital_deals.domain.getusecase;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.common.network.data.model.RestRequest;
import com.tokopedia.common.network.domain.RestRequestUseCase;
import com.tokopedia.digital_deals.view.model.response.DealsResponse;
import com.tokopedia.digital_deals.data.source.DealsUrl;
import com.tokopedia.digital_deals.view.model.response.AllBrandsResponse;
import com.tokopedia.digital_deals.view.presenter.DealsHomePresenter;
import com.tokopedia.usecase.RequestParams;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;


public class GetDealsListRequestUseCase extends RestRequestUseCase {

    private HashMap<String, Object> params;

    @Inject
    public GetDealsListRequestUseCase(){ }

    public void setRequestParams(HashMap<String, Object> requestParams) {
        this.params = requestParams;
    }

    @Override
    protected List<RestRequest> buildRequest(RequestParams requestParams) {
        List<RestRequest> tempRequest = new ArrayList<>();

        String param = String.valueOf(params.get(DealsHomePresenter.TAG));
        String url = DealsUrl.DEALS_DOMAIN + DealsUrl.HelperUrl.DEALS_LIST +"/" + param;
        //Request 1
        Type token = new TypeToken<DataResponse<DealsResponse>>() {
        }.getType();

        RestRequest restRequest1 = new RestRequest.Builder(url, token)
                .build();
        tempRequest.add(restRequest1);

        Type token2 = new TypeToken<DataResponse<AllBrandsResponse>>() {
        }.getType();

        params.remove(DealsHomePresenter.TAG);
        String url2 = DealsUrl.DEALS_DOMAIN + DealsUrl.HelperUrl.DEALS_LIST_SEARCH;
        RestRequest restRequest2 = new RestRequest.Builder(url2, token2)
                .setQueryParams(params)
                .build();

        tempRequest.add(restRequest2);
        return tempRequest;
    }
}
