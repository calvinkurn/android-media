package com.tokopedia.shop.open;

import android.content.Context;
import android.content.Intent;

import com.tokopedia.shop.open.view.activity.ShopOpenRoutingActivity;

/**
 * Created by yoshua on 07/06/18.
 */

public class ShopOpenRouter {
    public static Intent getIntentCreateEditShop(Context context){
        return ShopOpenRoutingActivity.getIntent(context);
    }
}
