package com.tokopedia.shop.open;

import android.content.Context;
import android.content.Intent;

import com.tokopedia.seller.shopsettings.edit.presenter.ShopSettingView;
import com.tokopedia.seller.shopsettings.edit.view.ShopEditorActivity;
import com.tokopedia.shop.open.view.activity.ShopOpenRoutingActivity;

/**
 * Created by yoshua on 07/06/18.
 */

public class ShopOpenRouter {
    public static Intent getIntentCreateEditShop(Context context, boolean isCreate, boolean logOutOnBack){
        Intent intent;
        if (isCreate) {
            intent = ShopOpenRoutingActivity.getIntent(context);
        } else {
            intent = new Intent(context, ShopEditorActivity.class);
            intent.putExtra(ShopSettingView.FRAGMENT_TO_SHOW, ShopSettingView.EDIT_SHOP_FRAGMENT_TAG);
            if (logOutOnBack) {
                intent.putExtra(ShopSettingView.ON_BACK, ShopSettingView.LOG_OUT);
            } else {
                intent.putExtra(ShopSettingView.ON_BACK, ShopSettingView.FINISH);
            }
        }
        return intent;
    }
}
