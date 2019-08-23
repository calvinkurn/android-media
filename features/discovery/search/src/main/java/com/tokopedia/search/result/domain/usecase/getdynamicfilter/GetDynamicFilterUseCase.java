package com.tokopedia.search.result.domain.usecase.getdynamicfilter;

import com.tokopedia.discovery.common.Repository;
import com.tokopedia.discovery.common.data.DynamicFilterModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

final class GetDynamicFilterUseCase extends UseCase<DynamicFilterModel> {

    private final Repository<DynamicFilterModel> dynamicFilterModelRepository;

    GetDynamicFilterUseCase(Repository<DynamicFilterModel> dynamicFilterModelRepository) {
        this.dynamicFilterModelRepository = dynamicFilterModelRepository;
    }

    @Override
    public Observable<DynamicFilterModel> createObservable(RequestParams requestParams) {
        return dynamicFilterModelRepository.query(requestParams.getParameters());
    }
}
