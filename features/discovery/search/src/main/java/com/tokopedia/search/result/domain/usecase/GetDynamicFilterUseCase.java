package com.tokopedia.search.result.domain.usecase;

import com.tokopedia.discovery.common.data.DynamicFilterModel;
import com.tokopedia.discovery.common.domain.Repository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

class GetDynamicFilterUseCase extends UseCase<DynamicFilterModel> {

    private final Repository<DynamicFilterModel> repository;

    public GetDynamicFilterUseCase(Repository<DynamicFilterModel> repository) {
        this.repository = repository;
    }

    @Override
    public Observable<DynamicFilterModel> createObservable(RequestParams requestParams) {
        return repository.query(requestParams.getParameters());
    }
}
