package com.tokopedia.posapp.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.posapp.domain.model.shop.ShopEtalaseDomain;

import java.util.List;

import rx.Observable;

/**
 * Created by okasurya on 9/18/17.
 */

public interface EtalaseRepository {
    Observable<List<ShopEtalaseDomain>> getEtalase(RequestParams requestParams);
}
