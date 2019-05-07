package com.tokopedia.search.result.domain.usecase;

import com.tokopedia.discovery.common.domain.Repository;
import com.tokopedia.search.result.domain.model.SearchProductModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

class SearchProductUseCase extends UseCase<SearchProductModel> {

    private Repository<SearchProductModel> repository;

    SearchProductUseCase(Repository<SearchProductModel> repository) {
        this.repository = repository;
    }

    @Override
    public Observable<SearchProductModel> createObservable(RequestParams requestParams) {
        return repository.query(requestParams);
    }
}
