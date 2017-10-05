package com.tokopedia.posapp.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.posapp.domain.model.shop.ShopDomain;

import rx.Observable;

/**
 * Created by okasurya on 8/3/17.
 */

public interface ShopRepository {
    Observable<ShopDomain> getShop(RequestParams requestParams);
}
