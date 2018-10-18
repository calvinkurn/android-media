package com.tokopedia.digital.newcart.domain;

import com.tokopedia.digital.newcart.domain.DigitalDealsRepository;
import com.tokopedia.digital.newcart.domain.model.DealProductsViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

public class DigitalDealGetProductsUseCase extends UseCase<DealProductsViewModel> {
    private DigitalDealsRepository digitalDealsRepository;
    private static final String PARAM_URL = "url";

    public DigitalDealGetProductsUseCase(DigitalDealsRepository digitalDealsRepository) {
        this.digitalDealsRepository = digitalDealsRepository;
    }

    @Override
    public Observable<DealProductsViewModel> createObservable(RequestParams requestParams) {
        return digitalDealsRepository.getProducts(requestParams.getString(PARAM_URL, ""));
    }

    public RequestParams createRequest(String url) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PARAM_URL, url);
        return requestParams;
    }
}
