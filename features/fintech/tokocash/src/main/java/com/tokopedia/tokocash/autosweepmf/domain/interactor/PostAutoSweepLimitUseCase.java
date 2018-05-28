package com.tokopedia.tokocash.autosweepmf.domain.interactor;

import com.google.gson.JsonObject;
import com.tokopedia.tokocash.autosweepmf.data.repository.AutoSweepRepositoryImpl;
import com.tokopedia.tokocash.autosweepmf.domain.model.AutoSweepLimitDomain;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

//TODO @lavekush need to follow `com.tokopedia.tkpd.tkpdreputation.reputationproduct.domain.usecase.PostReportUseCase` pattern for all use cases
public class PostAutoSweepLimitUseCase extends UseCase<AutoSweepLimitDomain> {
    private JsonObject body;
    private AutoSweepRepositoryImpl mRepository;

    @Inject
    public PostAutoSweepLimitUseCase(AutoSweepRepositoryImpl repository) {
        this.mRepository = repository;
    }

    @Override
    public Observable<AutoSweepLimitDomain> createObservable(RequestParams requestParams) {
        return mRepository.postAutoSweepLimit(requestParams);
    }
}
