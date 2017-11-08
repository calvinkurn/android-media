package com.tokopedia.digital.widget.presenter;

/**
 * Created by nabillasabbaha on 7/21/17.
 * Modified by rizkyfadillah at 10/6/17.
 */

public interface IDigitalWidgetStyle1Presenter {

    void fetchNumberList(String categoryId, boolean showLastOrder);

    void getOperatorById(String operatorId);

    void validatePhonePrefix(String phonePrefix, int categoryId, Boolean validatePrefix);

    void validateOperatorWithProducts(int categoryId, String operatorId);

    void validateOperatorWithoutProducts(int categoryId, String operatorId);

    void fetchDefaultProduct(String categoryId, String operatorId, String productId);
}
