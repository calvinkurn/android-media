package com.tokopedia.posapp.etalase.data.repository;

import com.tokopedia.posapp.base.domain.model.DataStatus;
import com.tokopedia.posapp.base.domain.model.ListDomain;
import com.tokopedia.posapp.etalase.data.source.EtalaseLocalSource;
import com.tokopedia.posapp.shop.domain.model.EtalaseDomain;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author okasurya on 4/2/18.
 */

public class EtalaseLocalRepository implements EtalaseRepository {
    private EtalaseLocalSource etalaseLocalSource;

    @Inject
    EtalaseLocalRepository(EtalaseLocalSource etalaseLocalSource) {
        this.etalaseLocalSource = etalaseLocalSource;
    }

    @Override
    public Observable<List<EtalaseDomain>> getEtalase(RequestParams requestParams) {
        return etalaseLocalSource.getAllEtalase();
    }

    @Override
    public Observable<DataStatus> storeEtalase(ListDomain<EtalaseDomain> data) {
        return etalaseLocalSource.storeEtalase(data);
    }
}
