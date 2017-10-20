package com.tokopedia.digital.widget.presenter;

import com.tokopedia.digital.widget.model.lastorder.LastOrder;
import com.tokopedia.digital.widget.model.product.Product;

/**
 * Created by nabillasabbaha on 8/8/17.
 */

public interface IBaseDigitalWidgetPresenter {

    boolean isAlreadyHaveLastOrderOnCache();

    LastOrder getLastOrderFromCache();

    void storeLastInstantCheckoutUsed(String categoryId, boolean checked);

    boolean isRecentInstantCheckoutUsed(String categoryId);

    boolean isAlreadyHaveLastOrderOnCacheByCategoryId(int categoryId);

    void storeLastClientNumberTyped(String categoryId, String clientNumber, Product selectedProduct);

    String getLastClientNumberTyped(String categoryId);

    String getLastOperatorSelected(String categoryId);

    String getLastProductSelected(String categoryId);
}
