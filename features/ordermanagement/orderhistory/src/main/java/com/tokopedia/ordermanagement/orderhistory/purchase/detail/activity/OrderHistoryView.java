package com.tokopedia.ordermanagement.orderhistory.purchase.detail.activity;

import com.tokopedia.ordermanagement.orderhistory.purchase.detail.model.history.viewmodel.OrderHistoryData;

/**
 * Created by kris on 11/17/17. Tokopedia
 */

public interface OrderHistoryView {

    void receivedHistoryData(OrderHistoryData data);

    void onLoadError(String message);

    void showMainViewLoadingPage();

    void hideMainViewLoadingPage();

}
