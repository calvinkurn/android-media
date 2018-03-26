package com.tokopedia.posapp.outlet.data.repository;

import com.tokopedia.posapp.outlet.domain.model.OutletDomain;
import com.tokopedia.usecase.RequestParams;

import rx.Observable;

/**
 * Created by okasurya on 7/31/17.
 */

public interface OutletRepository {
    Observable<OutletDomain> getOutlet(RequestParams requestParams);
}
