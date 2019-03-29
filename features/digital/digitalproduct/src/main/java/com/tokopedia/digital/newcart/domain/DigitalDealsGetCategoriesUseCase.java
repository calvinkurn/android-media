package com.tokopedia.digital.newcart.domain;

import com.tokopedia.digital.newcart.domain.model.DealCategoryViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import rx.Observable;

public class DigitalDealsGetCategoriesUseCase extends UseCase<List<DealCategoryViewModel>> {
    private DigitalDealsRepository repository;

    public DigitalDealsGetCategoriesUseCase(DigitalDealsRepository repository) {
        this.repository = repository;
    }

    @Override
    public Observable<List<DealCategoryViewModel>> createObservable(RequestParams requestParams) {
        return repository.getDealsCategory(requestParams.getParameters());
    }

    public RequestParams createRequestParam(String categoryId) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString("category_id", categoryId);
        requestParams.putString("vertical_id", "1");
        requestParams.putString("product_id", categoryId);
        return requestParams;
    }
}
