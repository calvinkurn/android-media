package com.tokopedia.posapp.outlet.domain.usecase;

import com.tokopedia.posapp.outlet.data.repository.OutletCloudRepository;
import com.tokopedia.posapp.outlet.data.repository.OutletRepository;
import com.tokopedia.posapp.outlet.domain.model.OutletDomain;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by okasurya on 7/31/17.
 */

public class GetOutletUseCase extends UseCase<OutletDomain> {
    private OutletRepository outletRepository;

    @Inject
    public GetOutletUseCase(OutletCloudRepository outletRepository) {
        this.outletRepository = outletRepository;
    }

    @Override
    public Observable<OutletDomain> createObservable(RequestParams requestParams) {
        return outletRepository.getOutlet(requestParams);
    }
}
