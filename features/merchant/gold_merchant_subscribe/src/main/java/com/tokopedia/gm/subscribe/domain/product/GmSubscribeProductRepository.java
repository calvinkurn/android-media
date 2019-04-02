package com.tokopedia.gm.subscribe.domain.product;

import com.tokopedia.gm.subscribe.domain.product.model.GmAutoSubscribeDomainModel;
import com.tokopedia.gm.subscribe.domain.product.model.GmProductDomainModel;

import java.util.List;

import rx.Observable;

/**
 * Created by sebastianuskh on 2/2/17.
 */
public interface GmSubscribeProductRepository {
    Observable<List<GmProductDomainModel>> getCurrentProductSelection();

    Observable<List<GmProductDomainModel>> getExtendProductSelection();

    Observable<GmProductDomainModel> getCurrentProductSelectedData(Integer productId);

    Observable<GmAutoSubscribeDomainModel> getExtendProductSelectedData(Integer autoSubscribeProductId, Integer productId);

    Observable<Boolean> clearGMProductCache();
}
