package com.tokopedia.product.manage.item.main.base.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.product.manage.item.main.base.data.model.ProductViewModel;
import com.tokopedia.product.manage.item.main.base.view.listener.ProductSubmitNotificationListener;

/**
 * @author sebastianuskh on 4/20/17.
 */

public abstract class AddProductServicePresenter extends BaseDaggerPresenter<AddProductServiceListener>{

    public abstract void uploadProduct(long draftProductId, ProductSubmitNotificationListener notificationCountListener);
    public abstract void uploadProduct(ProductViewModel productViewModel, ProductSubmitNotificationListener notificationCountListener);

}
