package com.tokopedia.shop;

import android.app.Application;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.shop.common.di.component.DaggerShopComponent;
import com.tokopedia.shop.common.di.component.ShopComponent;

/**
 * Created by nakama on 11/12/17.
 */

public class ShopComponentInstance {
    private static ShopComponent shopInfoComponent;

    public static ShopComponent getComponent(Application application) {
        if (shopInfoComponent == null) {
            shopInfoComponent = DaggerShopComponent.builder().baseAppComponent(
                    ((BaseMainApplication) application).getBaseAppComponent()).build();
        }
        return shopInfoComponent;
    }
}
