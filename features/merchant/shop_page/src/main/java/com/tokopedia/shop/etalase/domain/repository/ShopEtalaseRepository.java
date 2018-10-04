package com.tokopedia.shop.etalase.domain.repository;

import com.tokopedia.shop.etalase.data.source.cloud.model.EtalaseModel;
import com.tokopedia.shop.etalase.data.source.cloud.model.PagingListOther;
import com.tokopedia.shop.etalase.domain.model.ShopEtalaseRequestModel;

import rx.Observable;

/**
 * Created by sebastianuskh on 3/8/17.
 */

public interface ShopEtalaseRepository {

    Observable<PagingListOther<EtalaseModel>> getShopEtalaseList(ShopEtalaseRequestModel shopEtalaseRequestModel);
}
