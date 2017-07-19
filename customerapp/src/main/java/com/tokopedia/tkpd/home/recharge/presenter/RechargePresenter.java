package com.tokopedia.tkpd.home.recharge.presenter;


import com.tokopedia.core.database.recharge.recentOrder.LastOrder;

/**
 * @author Kulomady 05 on 7/13/2016.
 */
public interface RechargePresenter {

    void fetchDataProducts();

    void fetchRecentNumbers(int categoryId);

    void validatePhonePrefix(String phonePrefix, int categoryId, Boolean validatePrefix);

    void updateMinLenghAndOperator(String operatorId);

    void validateWithOperator(int categoryId, String operatorId);

    void validateOperatorWithoutProduct(int categoryId, String operatorId);

    void getListOperatorFromCategory( int categoryId);

    void getDefaultProduct(String categoryId, String operatorId, String productId);

    boolean isAlreadyHavePhonebookDataOnCache(String key);

    boolean isAlreadyHaveLastOrderDataOnCache();

    LastOrder getLastOrderFromCache();

    void storeLastInstantCheckoutUsed(String categoryId, boolean checked);

    boolean isRecentInstantCheckoutUsed(String categoryId);

    boolean isAlreadyHaveLastOrderDataOnCacheByCategoryId(int categoryId);

    void saveLastInputToCache(String key, String userLastInput);

    String getLastInputFromCache(String key);

    void clearRechargePhonebookCache();

}