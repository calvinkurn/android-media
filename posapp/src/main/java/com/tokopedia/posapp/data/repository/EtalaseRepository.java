package com.tokopedia.posapp.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.posapp.domain.model.etalase.EtalaseDomain;

import rx.Observable;

/**
 * Created by okasurya on 9/18/17.
 */

public interface EtalaseRepository {
    Observable<EtalaseDomain> getEtalase(RequestParams requestParams);
}
