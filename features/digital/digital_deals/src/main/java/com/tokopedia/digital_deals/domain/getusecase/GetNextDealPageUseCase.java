package com.tokopedia.digital_deals.domain.getusecase;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.network.data.model.response.DataResponse;
import com.tokopedia.common.network.data.model.RestRequest;
import com.tokopedia.common.network.domain.RestRequestUseCase;
import com.tokopedia.digital_deals.view.model.response.DealsResponse;
import com.tokopedia.digital_deals.view.presenter.DealsHomePresenter;
import com.tokopedia.digital_deals.view.utils.Utils;
import com.tokopedia.usecase.RequestParams;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

public class GetNextDealPageUseCase extends RestRequestUseCase {

    private RequestParams params;

    @Inject
    public GetNextDealPageUseCase() {

    }

    public void setRequestParams(RequestParams params) {
        this.params = params;
    }

    @Override
    protected List<RestRequest> buildRequest(RequestParams requestParams) {
        List<RestRequest> tempRequest=new ArrayList<>();
        HashMap<String, Object> map = params.getParameters();
        String nextUrl= String.valueOf(map.get(Utils.NEXT_URL));
        Type token=new TypeToken<DataResponse<DealsResponse>>(){

        }.getType();

        RestRequest restRequest=new RestRequest.Builder(nextUrl, token)
                .build();
        tempRequest.add(restRequest);
        return tempRequest;
    }

}
