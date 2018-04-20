package com.tokopedia.posapp.etalase.domain;

import com.tokopedia.posapp.etalase.data.repository.EtalaseRepository;
import com.tokopedia.posapp.shop.domain.model.EtalaseDomain;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by okasurya on 9/18/17.
 */

public class GetEtalaseUseCase extends UseCase<List<EtalaseDomain>> {
    private EtalaseRepository etalaseRepository;

    @Inject
    public GetEtalaseUseCase(EtalaseRepository etalaseRepository) {
        this.etalaseRepository = etalaseRepository;
    }

    @Override
    public Observable<List<EtalaseDomain>> createObservable(RequestParams requestParams) {
        return etalaseRepository.getEtalase(requestParams);
    }
}
