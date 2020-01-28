package com.tokopedia.digital_deals.domain.getusecase;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.network.data.model.response.DataResponse;
import com.tokopedia.common.network.data.model.RestRequest;
import com.tokopedia.common.network.domain.RestRequestUseCase;
import com.tokopedia.digital_deals.view.model.response.BrandDetailsResponse;
import com.tokopedia.digital_deals.view.presenter.BrandDetailsPresenter;
import com.tokopedia.usecase.RequestParams;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

public class GetBrandDetailsUseCase extends RestRequestUseCase {
    private RequestParams params;

    @Inject
    public GetBrandDetailsUseCase(){ }

    public void setRequestParams(RequestParams params) {
        this.params = params;
    }

    @Override
    protected List<RestRequest> buildRequest(RequestParams requestParams) {
        List<RestRequest> tempRequest = new ArrayList<>();

        String url = params.getString(BrandDetailsPresenter.TAG, "");
        //Request 1

        HashMap<String, Object> params1= params.getParameters();
        params1.remove(BrandDetailsPresenter.TAG);
        if(params.getBoolean("search_next", false))
            params1=new HashMap<>();
        Type token = new TypeToken<DataResponse<BrandDetailsResponse>>() {
        }.getType();

        RestRequest restRequest1 = new RestRequest.Builder(url, token)
                .setQueryParams(params1)
                .build();
        tempRequest.add(restRequest1);
        return tempRequest;
    }
}
