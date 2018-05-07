package com.tokopedia.posapp.outlet.domain.usecase;

import com.tokopedia.posapp.cart.domain.model.ATCStatusDomain;
import com.tokopedia.posapp.cart.domain.usecase.DeleteAllCartUseCase;
import com.tokopedia.posapp.outlet.data.repository.OutletCloudRepository;
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
    private DeleteAllCartUseCase deleteAllCartUseCase;

    @Inject
    SelectOutletUseCase(OutletLocalRepository outletRepository,
                        DeleteAllCartUseCase deleteAllCartUseCase) {
        this.outletRepository = outletRepository;
        this.deleteAllCartUseCase = deleteAllCartUseCase;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return outletRepository.selectOutlet(requestParams).flatMap(new Func1<Boolean, Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call(Boolean aBoolean) {
                return deleteAllCartUseCase
                        .createObservable(null)
                        .map(new Func1<ATCStatusDomain, Boolean>() {
                            @Override
                            public Boolean call(ATCStatusDomain atcStatusDomain) {
                                return true;
                            }
                        });
            }
        });
    }
}
