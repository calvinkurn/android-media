package com.tokopedia.digital_deals.domain.getusecase;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.common.network.data.model.RestRequest;
import com.tokopedia.common.network.domain.RestRequestUseCase;
import com.tokopedia.digital_deals.data.entity.response.homeresponse.DealsResponse;
import com.tokopedia.digital_deals.data.source.DealsBaseURL;
import com.tokopedia.digital_deals.data.source.DealsUrl;
import com.tokopedia.digital_deals.domain.DealsRepository;
import com.tokopedia.digital_deals.domain.model.dealdetailsdomainmodel.DealsDetailsDomain;
import com.tokopedia.digital_deals.view.activity.DealDetailsActivity;
import com.tokopedia.digital_deals.view.presenter.DealDetailsPresenter;
import com.tokopedia.digital_deals.view.presenter.DealsHomePresenter;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Observable;

public class GetDealDetailsUseCase extends RestRequestUseCase {
    private final DealsRepository dealsRepository;
    private RequestParams params;

    public GetDealDetailsUseCase(DealsRepository eventRepository) {
        super();
        this.dealsRepository = eventRepository;
    }

    public void setRequestParams(RequestParams requestParams) {
        this.params = requestParams;
    }

    @Override
    protected List<RestRequest> buildRequest() {
        List<RestRequest> tempRequest = new ArrayList<>();

        HashMap<String, Object> map = params.getParameters();
        String url = DealsBaseURL.DEALS_DOMAIN + DealsUrl.DEALS_PRODUCT +"/" + map.get(DealDetailsPresenter.TAG);
        //Request 1
        Type token = new TypeToken<DataResponse<DealsDetailsDomain>>() {
        }.getType();

        RestRequest restRequest1 = new RestRequest.Builder(url, token)
                .build();
        tempRequest.add(restRequest1);
        return tempRequest;
    }
}
