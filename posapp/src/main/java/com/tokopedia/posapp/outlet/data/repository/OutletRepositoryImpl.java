package com.tokopedia.posapp.outlet.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.posapp.outlet.data.factory.OutletFactory;
import com.tokopedia.posapp.outlet.domain.model.OutletDomain;

import rx.Observable;

/**
 * Created by okasurya on 7/31/17.
 */

public class OutletRepositoryImpl implements OutletRepository {
    private OutletFactory outletFactory;

    public OutletRepositoryImpl(OutletFactory outletFactory) {
        this.outletFactory = outletFactory;
    }

    @Override
    public Observable<OutletDomain> getOutlet(RequestParams requestParams) {
        return outletFactory.cloud().getOutlet(requestParams);
    }
}
