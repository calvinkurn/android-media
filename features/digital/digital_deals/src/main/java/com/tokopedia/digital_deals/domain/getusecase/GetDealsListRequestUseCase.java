package com.tokopedia.digital_deals.domain.getusecase;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.common.network.data.model.RestRequest;
import com.tokopedia.common.network.domain.RestRequestUseCase;
import com.tokopedia.digital_deals.data.entity.response.homeresponse.DealsResponse;
import com.tokopedia.digital_deals.data.source.DealsBaseURL;
import com.tokopedia.digital_deals.data.source.DealsUrl;
import com.tokopedia.digital_deals.domain.DealsRepository;
import com.tokopedia.digital_deals.domain.model.DealsDomain;
import com.tokopedia.digital_deals.domain.model.allbrandsdomainmodel.AllBrandsDomain;
import com.tokopedia.digital_deals.view.presenter.DealDetailsPresenter;
import com.tokopedia.digital_deals.view.presenter.DealsHomePresenter;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Observable;


public class GetDealsListRequestUseCase extends RestRequestUseCase {
    private final DealsRepository dealsRepository;
    private HashMap<String, Object> params;

    public GetDealsListRequestUseCase(DealsRepository eventRepository) {
        super();
        this.dealsRepository = eventRepository;
    }

    public void setRequestParams(HashMap<String, Object> requestParams) {
        this.params = requestParams;
    }

    @Override
    protected List<RestRequest> buildRequest() {
        List<RestRequest> tempRequest = new ArrayList<>();

        String param = String.valueOf(params.get(DealsHomePresenter.TAG));
        String url = DealsBaseURL.DEALS_DOMAIN + DealsUrl.DEALS_LIST +"/" + param;
        //Request 1
        Type token = new TypeToken<DataResponse<DealsResponse>>() {
        }.getType();

        RestRequest restRequest1 = new RestRequest.Builder(url, token)
                .build();
        tempRequest.add(restRequest1);

        Type token2 = new TypeToken<DataResponse<AllBrandsDomain>>() {
        }.getType();

        params.remove(DealsHomePresenter.TAG);
        String url2 = DealsBaseURL.DEALS_DOMAIN + DealsUrl.DEALS_LIST_SEARCH;
        RestRequest restRequest2 = new RestRequest.Builder(url2, token2)
                .setQueryParams(params)
                .build();

        tempRequest.add(restRequest2);
        return tempRequest;
    }
}
