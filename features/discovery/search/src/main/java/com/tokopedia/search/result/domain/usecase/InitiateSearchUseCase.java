package com.tokopedia.search.result.domain.usecase;

import com.tokopedia.discovery.common.domain.Repository;
import com.tokopedia.discovery.newdiscovery.domain.model.InitiateSearchModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

class InitiateSearchUseCase extends UseCase<InitiateSearchModel> {

    private Repository<InitiateSearchModel> repository;

    InitiateSearchUseCase(Repository<InitiateSearchModel> repository) {
        this.repository = repository;
    }

    @Override
    public Observable<InitiateSearchModel> createObservable(RequestParams requestParams) {
        return repository.query(requestParams);
    }
}