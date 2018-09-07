package com.tokopedia.tkpd.deeplink.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;

import rx.Observable;

/**
 * Created by okasurya on 1/5/18.
 */

public interface ShopInfoRepository {
    Observable<ShopModel> getShopInfo(RequestParams requestParams);
}
