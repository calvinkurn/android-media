package com.tokopedia.search.result.domain.usecase.initiatesearch;

import com.tokopedia.discovery.common.domain.Repository;
import com.tokopedia.discovery.newdiscovery.domain.model.InitiateSearchModel;
import com.tokopedia.search.utils.UrlParamUtils;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

import static com.tokopedia.discovery.common.constants.SearchConstant.GQL.KEY_PARAMS;

class InitiateSearchUseCase extends UseCase<InitiateSearchModel> {

    private Repository<InitiateSearchModel> repository;

    InitiateSearchUseCase(Repository<InitiateSearchModel> repository) {
        this.repository = repository;
    }

    @Override
    public Observable<InitiateSearchModel> createObservable(RequestParams requestParams) {
        return repository.query(requestParams.getParameters());
    }
}