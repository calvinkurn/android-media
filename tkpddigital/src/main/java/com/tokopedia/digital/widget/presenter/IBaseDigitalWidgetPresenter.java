package com.tokopedia.digital.widget.presenter;

import com.tokopedia.digital.widget.model.product.Product;

/**
 * Created by nabillasabbaha on 8/8/17.
 * Modified by rizkyfadillah at 10/6/17.
 */

public interface IBaseDigitalWidgetPresenter {

    void storeLastInstantCheckoutUsed(String categoryId, boolean checked);

    boolean isRecentInstantCheckoutUsed(String categoryId);

    void storeLastClientNumberTyped(String categoryId, String clientNumber, Product selectedProduct);

    void storeLastClientNumberTyped(String categoryId, String operatorId, String clientNumber,
                                           String productId);

    String getLastOperatorSelected(String categoryId);

    String getLastClientNumberTyped(String categoryId);

    String getLastProductSelected(String categoryId);

}
