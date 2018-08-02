package com.tokopedia.digital_deals.domain.getusecase;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.common.network.data.model.RestRequest;
import com.tokopedia.common.network.domain.RestRequestUseCase;
import com.tokopedia.digital_deals.data.source.DealsUrl;
import com.tokopedia.digital_deals.view.model.response.CategoryDetailsResponse;
import com.tokopedia.digital_deals.view.model.response.FavouriteDealsResponse;
import com.tokopedia.digital_deals.view.utils.Utils;
import com.tokopedia.usecase.RequestParams;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class GetFavouriteDealsUseCase extends RestRequestUseCase {
    private RequestParams params;

    @Inject
    public GetFavouriteDealsUseCase() {
    }

    public void setRequestParams(RequestParams params) {
        this.params = params;
    }

    @Override
    protected List<RestRequest> buildRequest() {
        List<RestRequest> tempRequest = new ArrayList<>();
        String url;
        if (params == null) {
            url = DealsUrl.DEALS_DOMAIN + DealsUrl.HelperUrl.DEALS_LIKED_PRODUCT;
        } else {
            url = params.getString(Utils.NEXT_URL, "");
        }
        Type token = new TypeToken<DataResponse<FavouriteDealsResponse>>() {
        }.getType();
        RestRequest restRequest1 = new RestRequest.Builder(url, token)
                .build();
        tempRequest.add(restRequest1);
        return tempRequest;
    }
}
