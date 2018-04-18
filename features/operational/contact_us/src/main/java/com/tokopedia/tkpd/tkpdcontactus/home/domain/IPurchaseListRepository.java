package com.tokopedia.tkpd.tkpdcontactus.home.domain;

import com.tokopedia.tkpd.tkpdcontactus.common.data.BuyerPurchaseList;

import java.util.List;

import rx.Observable;

/**
 * Created by sandeepgoyal on 11/04/18.
 */

public interface IPurchaseListRepository {
    Observable<List<BuyerPurchaseList>> getPurchaseList();
}
