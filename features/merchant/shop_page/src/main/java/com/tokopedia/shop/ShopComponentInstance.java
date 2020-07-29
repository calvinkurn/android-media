package com.tokopedia.shop;

import android.app.Application;
import android.content.Context;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.shop.common.di.component.DaggerShopComponent;
import com.tokopedia.shop.common.di.component.ShopComponent;
import com.tokopedia.shop.common.di.module.ShopModule;

/**
 * Created by nakama on 11/12/17.
 */

public class ShopComponentInstance {
    private static ShopComponent shopInfoComponent;

    public static ShopComponent getComponent(Application application, Context activityContext) {
        if (shopInfoComponent == null) {
            shopInfoComponent = DaggerShopComponent.builder().baseAppComponent(
                    ((BaseMainApplication) application).getBaseAppComponent()).shopModule(new ShopModule(activityContext)).build();
        }
        return shopInfoComponent;
    }
}
