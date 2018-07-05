package com.tokopedia.digital_deals.domain.getusecase;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.common.network.data.model.RestRequest;
import com.tokopedia.common.network.domain.RestRequestUseCase;
import com.tokopedia.digital_deals.data.source.DealsBaseURL;
import com.tokopedia.digital_deals.data.source.DealsUrl;
import com.tokopedia.digital_deals.domain.DealsRepository;
import com.tokopedia.digital_deals.domain.model.allbrandsdomainmodel.AllBrandsDomain;
import com.tokopedia.digital_deals.domain.model.categorydomainmodel.CategoryDetailsDomain;
import com.tokopedia.digital_deals.view.presenter.DealsHomePresenter;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Observable;

public class GetCategoryDetailRequestUseCase extends RestRequestUseCase {
    private final DealsRepository dealsRepository;
    private RequestParams params;

    public GetCategoryDetailRequestUseCase(DealsRepository eventRepository) {
        super();
        this.dealsRepository = eventRepository;
    }

    public void setRequestParams(RequestParams params) {
        this.params = params;
    }

    @Override
    protected List<RestRequest> buildRequest() {
        List<RestRequest> tempRequest = new ArrayList<>();

        HashMap<String, Object> requestParams=params.getParameters();
        String url=String.valueOf(requestParams.get(DealsHomePresenter.TAG));
        requestParams.remove(DealsHomePresenter.TAG);
        Type token = new TypeToken<DataResponse<CategoryDetailsDomain>>() {
        }.getType();

        RestRequest restRequest1 = new RestRequest.Builder(url, token)
                .setQueryParams(params.getParameters())
                .build();
        tempRequest.add(restRequest1);
        return tempRequest;
    }
}
