package com.tokopedia.posapp.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.posapp.domain.model.DataStatus;
import com.tokopedia.posapp.domain.model.ListDomain;
import com.tokopedia.posapp.domain.model.shop.EtalaseDomain;

import java.util.List;

import rx.Observable;

/**
 * Created by okasurya on 9/18/17.
 */

public interface EtalaseRepository {
    Observable<List<EtalaseDomain>> getEtalase(RequestParams requestParams);

    Observable<List<EtalaseDomain>> getEtalaseCache();

    Observable<DataStatus> storeEtalaseToCache(ListDomain<EtalaseDomain> data);
}
