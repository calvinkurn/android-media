package com.tokopedia.instantloan.domain.interactor;

import com.google.gson.JsonObject;
import com.tokopedia.instantloan.data.repository.InstantLoanDataRepository;
import com.tokopedia.instantloan.domain.model.PhoneDataModelDomain;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by lavekush on 21/03/18.
 */

public class PostPhoneDataUseCase extends UseCase<PhoneDataModelDomain> {
    private final InstantLoanDataRepository mRepository;
    private JsonObject body;

    @Inject
    public PostPhoneDataUseCase(InstantLoanDataRepository repository) {
        this.mRepository = repository;
    }

    @Override
    public Observable<PhoneDataModelDomain> createObservable(RequestParams requestParams) {
        return mRepository.postPhoneData(body);
    }

    public void setBody(JsonObject body) {
        this.body = body;
    }
}
