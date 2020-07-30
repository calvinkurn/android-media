package com.tokopedia.product.manage.item.main.base.view.presenter;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.product.manage.item.main.base.view.listener.ProductSubmitNotificationListener;

/**
 * @author sebastianuskh on 4/20/17.
 */

public interface AddProductServiceListener extends CustomerView {

    void onSuccessAddProduct(ProductSubmitNotificationListener notificationCountListener);

    void onFailedAddProduct(Throwable t, ProductSubmitNotificationListener notificationCountListener);

}