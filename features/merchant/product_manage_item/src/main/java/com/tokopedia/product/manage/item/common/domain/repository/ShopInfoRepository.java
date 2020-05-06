package com.tokopedia.product.manage.item.common.domain.repository;

import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.product.manage.item.common.domain.model.AddProductShopInfoDomainModel;

import rx.Observable;

/**
 * Created by sebastianuskh on 3/8/17.
 */

public interface ShopInfoRepository {
    Observable<AddProductShopInfoDomainModel> getAddProductShopInfo();
    String getShopId();
    Observable<ShopModel> getShopInfo();
}
