package com.tokopedia.posapp.outlet.data.repository;

import com.tokopedia.posapp.outlet.data.source.OutletLocalSource;
import com.tokopedia.posapp.outlet.domain.model.OutletDomain;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author okasurya on 5/7/18.
 */

public class OutletLocalRepository implements OutletRepository {
    private OutletLocalSource outletLocalSource;

    @Inject
    OutletLocalRepository(OutletLocalSource outletLocalSource) {
        this.outletLocalSource = outletLocalSource;
    }

    @Override
    public Observable<OutletDomain> getOutlet(RequestParams requestParams) {
        return Observable.error(new Exception("Method not implemented"));
    }

    @Override
    public Observable<Boolean> selectOutlet(RequestParams requestParams) {
        return outletLocalSource.selectOutlet(requestParams);
    }

}
