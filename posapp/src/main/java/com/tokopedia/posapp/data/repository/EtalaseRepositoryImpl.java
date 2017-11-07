package com.tokopedia.posapp.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.posapp.data.factory.EtalaseFactory;
import com.tokopedia.posapp.domain.model.DataStatus;
import com.tokopedia.posapp.domain.model.ListDomain;
import com.tokopedia.posapp.domain.model.shop.EtalaseDomain;

import java.util.List;

import rx.Observable;

/**
 * Created by okasurya on 9/19/17.
 */

public class EtalaseRepositoryImpl implements EtalaseRepository {
    EtalaseFactory etalaseFactory;

    public EtalaseRepositoryImpl(EtalaseFactory etalaseFactory) {
        this.etalaseFactory = etalaseFactory;
    }

    @Override
    public Observable<List<EtalaseDomain>> getEtalase(RequestParams requestParams) {
        return etalaseFactory.cloud().getEtalase(requestParams);
    }

    @Override
    public Observable<List<EtalaseDomain>> getEtalaseCache() {
        return etalaseFactory.local().getAllEtalase();
    }

    @Override
    public Observable<DataStatus> storeEtalaseToCache(ListDomain<EtalaseDomain> data) {
        return etalaseFactory.local().storeEtalase(data);
    }
}
