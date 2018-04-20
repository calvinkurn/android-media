package com.tokopedia.posapp.etalase.data.repository;

import com.tokopedia.posapp.base.domain.model.DataStatus;
import com.tokopedia.posapp.base.domain.model.ListDomain;
import com.tokopedia.posapp.etalase.data.source.EtalaseCloudSource;
import com.tokopedia.posapp.shop.domain.model.EtalaseDomain;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by okasurya on 9/19/17.
 */

public class EtalaseCloudRepository implements EtalaseRepository {
    private EtalaseCloudSource etalaseCloudSource;

    @Inject
    public EtalaseCloudRepository(EtalaseCloudSource etalaseCloudSource) {
        this.etalaseCloudSource = etalaseCloudSource;
    }

    @Override
    public Observable<List<EtalaseDomain>> getEtalase(RequestParams requestParams) {
        return etalaseCloudSource.getEtalase(requestParams);
    }

    @Override
    public Observable<DataStatus> storeEtalase(ListDomain<EtalaseDomain> data) {
        return null;
    }
}
