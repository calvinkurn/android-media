package com.tokopedia.shop.open;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.tokopedia.shop.open.view.activity.ShopOpenDomainActivity;

/**
 * Created by hendry on 03/09/18.
 */
public class ShopOpenInternalRouter {

    public static Intent getOpenShopIntent(Context context) {
        return ShopOpenDomainActivity.getIntent(context);
    }
}
