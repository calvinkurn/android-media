package com.tokopedia.shop;

import android.content.Context;
import android.content.Intent;

import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.shop.newproduct.view.activity.ShopProductListActivity;

/**
 * Created by hendry on 03/09/18.
 */
public class ShopPageInternalRouter {

    public static Intent getShopPageIntent(Context context, String shopId) {
        return RouteManager.getIntent(context, ApplinkConst.SHOP, shopId);
    }

    public static Intent getShoProductListIntent(Context context, String shopId, String keyword, String etalaseId) {
        return ShopProductListActivity.createIntent(context, shopId, keyword, etalaseId, "", "");
    }
}
