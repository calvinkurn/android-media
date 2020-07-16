package com.tokopedia.topads.dashboard.data.repository;


import com.tokopedia.topads.common.model.AddProductShopInfoDomainModel;
import com.tokopedia.topads.common.model.shopmodel.ShopModel;

import rx.Observable;

/**
 * Created by sebastianuskh on 3/8/17.
 */

public interface ShopInfoRepository {
    Observable<AddProductShopInfoDomainModel> getAddProductShopInfo();
    String getShopId();
    Observable<ShopModel> getShopInfo();
}
