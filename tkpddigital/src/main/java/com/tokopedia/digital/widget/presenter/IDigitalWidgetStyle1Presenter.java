package com.tokopedia.digital.widget.presenter;

import com.tokopedia.core.database.recharge.recentOrder.LastOrder;

/**
 * Created by nabillasabbaha on 7/21/17.
 */

public interface IDigitalWidgetStyle1Presenter {

    void fetchNumberList(String categoryId);

    void getOperatorById(String operatorId);

    void validatePhonePrefix(String phonePrefix, int categoryId, Boolean validatePrefix);

    void validateOperatorWithProducts(int categoryId, String operatorId);

    void validateOperatorWithoutProducts(int categoryId, String operatorId);

    void fetchDefaultProduct(String categoryId, String operatorId, String productId);

    void onDestroy();
}
