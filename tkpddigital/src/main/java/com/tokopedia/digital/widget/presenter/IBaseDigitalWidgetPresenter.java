package com.tokopedia.digital.widget.presenter;

import com.tokopedia.core.database.recharge.product.Product;
import com.tokopedia.core.database.recharge.recentOrder.LastOrder;

/**
 * Created by nabillasabbaha on 8/8/17.
 */

public interface IBaseDigitalWidgetPresenter {

//    void fetchNumberList(String categoryId);

    boolean isAlreadyHaveLastOrderOnCache();

//    LastOrder getLastOrderFromCache();

    void storeLastInstantCheckoutUsed(String categoryId, boolean checked);

    boolean isRecentInstantCheckoutUsed(String categoryId);

    boolean isAlreadyHaveLastOrderOnCacheByCategoryId(int categoryId);

    void storeLastClientNumberTyped(String categoryId, String clientNumber, Product selectedProduct);

    String getLastClientNumberTyped(String categoryId);

    String getLastOperatorSelected(String categoryId);

    String getLastProductSelected(String categoryId);
}
