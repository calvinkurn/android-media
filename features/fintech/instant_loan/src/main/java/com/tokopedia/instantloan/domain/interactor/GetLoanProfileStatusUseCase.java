package com.tokopedia.instantloan.domain.interactor;

import com.tokopedia.instantloan.data.repository.InstantLoanDataRepository;
import com.tokopedia.instantloan.domain.model.LoanProfileStatusModelDomain;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by lavekush on 21/03/18.
 */

public class GetLoanProfileStatusUseCase extends UseCase<LoanProfileStatusModelDomain> {
    private final InstantLoanDataRepository mRepository;

    @Inject
    public GetLoanProfileStatusUseCase(InstantLoanDataRepository repository) {
        this.mRepository = repository;
    }

    @Override
    public Observable<LoanProfileStatusModelDomain> createObservable(RequestParams requestParams) {
        return mRepository.getLoanProfileStatus();
    }
}
