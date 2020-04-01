package com.tokopedia.digital.newcart.domain;

import com.tokopedia.digital.newcart.domain.model.DealProductsViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

public class DigitalDealGetProductsUseCase extends UseCase<DealProductsViewModel> {
    private DigitalDealsRepository digitalDealsRepository;
    private static final String PARAM_URL = "url";
    private static final String PARAM_CATEGORY_NAME = "name";

    public DigitalDealGetProductsUseCase(DigitalDealsRepository digitalDealsRepository) {
        this.digitalDealsRepository = digitalDealsRepository;
    }

    @Override
    public Observable<DealProductsViewModel> createObservable(RequestParams requestParams) {
        return digitalDealsRepository.getProducts(requestParams.getString(PARAM_URL, ""),
                requestParams.getString(PARAM_CATEGORY_NAME, ""));
    }

    public RequestParams createRequest(String url, String categoryName) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PARAM_URL, url);
        requestParams.putString(PARAM_CATEGORY_NAME, url);
        return requestParams;
    }
}
