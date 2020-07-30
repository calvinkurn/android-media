package com.tokopedia.digital_deals.domain.getusecase;

import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.network.data.model.response.DataResponse;
import com.tokopedia.common.network.data.model.RestRequest;
import com.tokopedia.common.network.domain.RestRequestUseCase;
import com.tokopedia.digital_deals.data.source.DealsUrl;
import com.tokopedia.digital_deals.view.model.response.LocationResponse;
import com.tokopedia.usecase.RequestParams;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

public class GetLocationListRequestUseCase extends RestRequestUseCase {
    private RequestParams params;

    @Inject
    public GetLocationListRequestUseCase(){ }

    public void setRequestParams(RequestParams params) {
        this.params = params;
    }

    @Override
    protected List<RestRequest> buildRequest(RequestParams requestParams) {
        List<RestRequest> tempRequest = new ArrayList<>();
        String url = params.getString("url","");
        HashMap<String, Object> params1 = params.getParameters();
        params1.remove("url");

        if (TextUtils.isEmpty(url)) {
            url = DealsUrl.DEALS_DOMAIN + DealsUrl.HelperUrl.DEALS_LOCATIONS;
            params1.put("page_no", "1");
        }
        //Request 1
        Type token = new TypeToken<DataResponse<LocationResponse>>() {
        }.getType();

        RestRequest restRequest1 = new RestRequest.Builder(url, token)
                .setQueryParams(params1)
                .build();
        tempRequest.add(restRequest1);
        return tempRequest;
    }
}
