package com.tokopedia.digital.widget.presenter;

/**
 * Created by nabillasabbaha on 7/21/17.
 */

public interface IDigitalWidgetStyle2Presenter {

    void fetchNumberList(String categoryId);

    void getOperatorById(String operatorId);

    void validateOperatorWithProducts(int categoryId, String operatorId);

    void fetchDefaultProduct(String categoryId, String operatorId, String productId);

    void fetchOperatorByCategory(int categoryId);

    void onDestroy();
}
