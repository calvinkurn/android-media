package com.tokopedia.core.recharge.presenter;

import com.tokopedia.core.recharge.model.recentOrder.LastOrder;

/**
 * @author Kulomady 05 on 7/13/2016.
 */
public interface RechargePresenter {

    void fetchDataProducts();

    void fetchRecentNumbers(int categoryId);

    void validatePhonePrefix(String phonePrefix, int categoryId, Boolean validatePrefix);

    void updateMinLenghAndOperator(String operatorId);

    void validateWithDefaultOperator( int categoryId, String operatorId);

    boolean isAlreadyHavePhonebookDataOnCache(String key);

    boolean isAlreadyHaveLastOrderDataOnCache();

    LastOrder getLastOrderFromCache();

    void saveLastInputToCache(String key, String userLastInput);

    String getLastInputFromCache(String key);

    void clearRechargePhonebookCache();

}
