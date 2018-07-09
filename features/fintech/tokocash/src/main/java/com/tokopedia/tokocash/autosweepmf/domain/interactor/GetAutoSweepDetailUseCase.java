package com.tokopedia.tokocash.autosweepmf.domain.interactor;

import com.tokopedia.tokocash.autosweepmf.data.repository.AutoSweepRepositoryImpl;
import com.tokopedia.tokocash.autosweepmf.domain.model.AutoSweepDetailDomain;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

public class GetAutoSweepDetailUseCase extends UseCase<AutoSweepDetailDomain> {
    private final AutoSweepRepositoryImpl mRepository;

    @Inject
    public GetAutoSweepDetailUseCase(AutoSweepRepositoryImpl repository) {
        this.mRepository = repository;
    }


    @Override
    public Observable<AutoSweepDetailDomain> createObservable(RequestParams requestParams) {
        return mRepository.getAutoSweepDetail();
    }
}
