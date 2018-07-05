package com.tokopedia.digital_deals.domain.getusecase;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.common.network.data.model.RestRequest;
import com.tokopedia.common.network.domain.RestRequestUseCase;
import com.tokopedia.digital_deals.data.source.DealsBaseURL;
import com.tokopedia.digital_deals.data.source.DealsUrl;
import com.tokopedia.digital_deals.domain.DealsRepository;
import com.tokopedia.digital_deals.domain.model.dealdetailsdomainmodel.DealsDetailsDomain;
import com.tokopedia.digital_deals.domain.model.searchdomainmodel.SearchDomainModel;
import com.tokopedia.digital_deals.view.presenter.DealDetailsPresenter;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Observable;



public class GetSearchDealsListRequestUseCase extends RestRequestUseCase {
    private final DealsRepository dealsRepository;
    public final String TAG = "tags";
    private RequestParams params;

    public GetSearchDealsListRequestUseCase(DealsRepository dealsRepository) {
        super();
        this.dealsRepository = dealsRepository;
    }

    public void setRequestParams(RequestParams params) {
        this.params = params;
    }

    @Override
    protected List<RestRequest> buildRequest() {
        List<RestRequest> tempRequest = new ArrayList<>();

        String url = DealsBaseURL.DEALS_DOMAIN + DealsUrl.DEALS_LIST_SEARCH;
        //Request 1
        Type token = new TypeToken<DataResponse<SearchDomainModel>>() {
        }.getType();

        RestRequest restRequest1 = new RestRequest.Builder(url, token)
                .setQueryParams(params.getParameters())
                .build();
        tempRequest.add(restRequest1);
        return tempRequest;
    }
}
