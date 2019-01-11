package com.tokopedia.logisticinsurance.domain;

import com.tokopedia.logisticdata.data.repository.InsuranceTnCRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Irfan Khoirul on 11/12/17.
 */

public class InsuranceTnCUseCase extends UseCase<String> {

    private final InsuranceTnCRepository repository;

    @Inject
    public InsuranceTnCUseCase(InsuranceTnCRepository repository) {
        this.repository = repository;
    }

    @Override
    public Observable<String> createObservable(RequestParams requestParams) {
        return repository.getInsuranceTnc();
    }

}
