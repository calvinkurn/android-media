package com.tokopedia.posapp.outlet.data.repository;

import com.tokopedia.posapp.outlet.data.source.OutletCloudSource;
import com.tokopedia.posapp.outlet.domain.model.OutletDomain;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by okasurya on 7/31/17.
 */

public class OutletCloudRepository implements OutletRepository {
    private OutletCloudSource outletCloudSource;

    @Inject
    public OutletCloudRepository(OutletCloudSource outletCloudSource) {
        this.outletCloudSource = outletCloudSource;
    }

    @Override
    public Observable<OutletDomain> getOutlet(RequestParams requestParams) {
        return outletCloudSource.getOutlet(requestParams);
    }
}
