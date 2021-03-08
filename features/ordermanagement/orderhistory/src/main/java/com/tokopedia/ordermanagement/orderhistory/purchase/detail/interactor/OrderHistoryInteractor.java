package com.tokopedia.ordermanagement.orderhistory.purchase.detail.interactor;

import com.tokopedia.ordermanagement.orderhistory.purchase.detail.model.history.viewmodel.OrderHistoryData;

import java.util.HashMap;

import rx.Subscriber;

/**
 * Created by kris on 11/17/17. Tokopedia
 */

public interface OrderHistoryInteractor {

    void requestOrderHistoryData(Subscriber<OrderHistoryData> subscriber,
                                 HashMap<String, Object> params);

    void onViewDestroyed();
}
