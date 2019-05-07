package com.tokopedia.search.result.domain.usecase;

import com.tokopedia.discovery.common.data.DynamicFilterModel;
import com.tokopedia.search.result.domain.repository.DynamicFilterRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

class GetDynamicFilterUseCase extends UseCase<DynamicFilterModel> {

    private final DynamicFilterRepository repository;

    public GetDynamicFilterUseCase(DynamicFilterRepository repository) {
        this.repository = repository;
    }

    @Override
    public Observable<DynamicFilterModel> createObservable(RequestParams requestParams) {
        return repository.getDynamicFilter(requestParams.getParameters());
    }
}
