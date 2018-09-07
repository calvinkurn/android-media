package com.tokopedia.contactus.home.domain;

import com.tokopedia.contactus.common.data.BuyerPurchaseList;

import java.util.List;

import rx.Observable;

/**
 * Created by sandeepgoyal on 11/04/18.
 */

public interface IPurchaseListRepository {
    Observable<List<BuyerPurchaseList>> getPurchaseList();
    Observable<List<BuyerPurchaseList>> getSellerPurchaseList();
}
