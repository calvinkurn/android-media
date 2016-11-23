package com.tokopedia.home.recharge.presenter;


import com.tokopedia.core.database.recharge.recentOrder.LastOrder;

/**
 * @author Kulomady 05 on 7/13/2016.
 */
public interface RechargePresenter {

    void fetchDataProducts();

    void fetchRecentNumbers(int categoryId);

    void validatePhonePrefix(String phonePrefix, int categoryId, Boolean validatePrefix);

    boolean isAlreadyHavePhonebookDataOnCache(String key);

    boolean isAlreadyHaveLastOrderDataOnCache();

    LastOrder getLastOrderFromCache();

    void saveLastInputToCache(String key, String userLastInput);

    String getLastInputFromCache(String key);

    void clearRechargePhonebookCache();

}
