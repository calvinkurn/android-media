package com.tokopedia.posapp.cache.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.posapp.base.domain.model.DataStatus;
import com.tokopedia.posapp.base.domain.model.ListDomain;
import com.tokopedia.posapp.shop.domain.model.EtalaseDomain;

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
