package com.tokopedia.product.manage.item.utils;

import android.app.Application;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.product.manage.item.common.di.component.DaggerProductComponent;
import com.tokopedia.product.manage.item.common.di.component.ProductComponent;
import com.tokopedia.product.manage.item.common.di.module.ProductModule;

/**
 * Created by nakama on 11/12/17.
 */

public class ProductEditItemComponentInstance {
    private static ProductComponent productEditItemComponent;

    public static ProductComponent getComponent(Application application) {
        if (productEditItemComponent == null) {
            productEditItemComponent = DaggerProductComponent.builder().productModule(
                    new ProductModule()).appComponent(((MainApplication) application).getApplicationComponent()).build();
        }
        return productEditItemComponent;
    }
}
