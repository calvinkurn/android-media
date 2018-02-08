package com.tokopedia.digital.widget.presenter;

/**
 * Created by nabillasabbaha on 7/21/17.
 * Modified by rizkyfadillah at 10/6/17.
 */

public interface IDigitalWidgetStyle1Presenter {

    void fetchNumberList(String categoryId, boolean showLastOrder);

    void getOperatorAndProductsByPrefix(String phonePrefix, int categoryId, boolean validatePrefix);

    void getOperatorAndProductsByOperatorId(int categoryId, String operatorId);

    void getOperatorById(String operatorId);

    void fetchDefaultProduct(String categoryId, String operatorId, String productId);

}
