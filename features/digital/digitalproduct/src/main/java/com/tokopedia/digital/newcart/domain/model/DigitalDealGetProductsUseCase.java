package com.tokopedia.digital.newcart.domain.model;

import com.tokopedia.digital.newcart.domain.DigitalDealsRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

public class DigitalDealGetProductsUseCase extends UseCase<DealProductsViewModel> {
    private DigitalDealsRepository digitalDealsRepository;

    public DigitalDealGetProductsUseCase(DigitalDealsRepository digitalDealsRepository) {
        this.digitalDealsRepository = digitalDealsRepository;
    }

    @Override
    public Observable<DealProductsViewModel> createObservable(RequestParams requestParams) {
        return digitalDealsRepository.getProducts(requestParams.getString("url", ""));
    }
}
