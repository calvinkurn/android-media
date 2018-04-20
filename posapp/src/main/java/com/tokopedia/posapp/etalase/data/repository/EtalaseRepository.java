package com.tokopedia.posapp.etalase.data.repository;

import com.tokopedia.posapp.base.domain.model.DataStatus;
import com.tokopedia.posapp.base.domain.model.ListDomain;
import com.tokopedia.posapp.shop.domain.model.EtalaseDomain;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import rx.Observable;

/**
 * Created by okasurya on 9/18/17.
 */

public interface EtalaseRepository {
    Observable<List<EtalaseDomain>> getEtalase(RequestParams requestParams);

    Observable<DataStatus> storeEtalase(ListDomain<EtalaseDomain> data);
}
