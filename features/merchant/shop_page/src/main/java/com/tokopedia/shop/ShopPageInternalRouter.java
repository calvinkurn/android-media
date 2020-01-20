package com.tokopedia.shop;

import android.content.Context;
import android.content.Intent;

import com.tokopedia.shop.page.view.activity.ShopPageActivity;
import com.tokopedia.shop.product.view.activity.ShopProductListActivity;

/**
 * Created by hendry on 03/09/18.
 */
public class ShopPageInternalRouter {

    public static Intent getShopPageIntent(Context context, String shopId) {
        return ShopPageActivity.createIntent(context, shopId);
    }

    public static Intent getShoProductListIntent(Context context, String shopId, String keyword, String etalaseId) {
        return ShopProductListActivity.createIntent(context, shopId, keyword, etalaseId, "");
    }
}
