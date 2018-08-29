package com.tokopedia.posapp.outlet.domain.usecase;

import com.tokopedia.posapp.cart.domain.model.ATCStatusDomain;
import com.tokopedia.posapp.cart.domain.usecase.DeleteAllCartUseCase;
import com.tokopedia.posapp.outlet.data.repository.OutletLocalRepository;
import com.tokopedia.posapp.outlet.data.repository.OutletRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author okasurya on 5/7/18.
 */

public class SelectOutletUseCase extends UseCase<Boolean> {
    private OutletRepository outletRepository;

    @Inject
    SelectOutletUseCase(OutletLocalRepository outletRepository) {
        this.outletRepository = outletRepository;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return outletRepository.selectOutlet(requestParams);
    }
}
