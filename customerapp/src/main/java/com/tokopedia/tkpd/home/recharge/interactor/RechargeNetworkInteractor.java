package com.tokopedia.tkpd.home.recharge.interactor;

import com.tokopedia.core.database.recharge.recentNumber.RecentData;
import com.tokopedia.core.database.recharge.recentOrder.LastOrder;

import java.util.Map;

/**
 * @author ricoharisin on 7/4/16.
 */
public interface RechargeNetworkInteractor {

    void getRecentNumbers(Map<String,String> params, OnGetRecentNumbersListener listener);

    void getLastOrder(Map<String, String>params, OnGetRecentOrderListener listener);

    interface OnGetRecentNumbersListener {
        void onGetRecentNumbersSuccess(RecentData recentNumber);
        void onNetworkError();
    }

    interface OnGetRecentOrderListener {
        void onGetLastOrderSuccess(LastOrder lastOrder);
        void onNetworkError();
    }
}
