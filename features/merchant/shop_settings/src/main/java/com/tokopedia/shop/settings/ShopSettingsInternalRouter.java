package com.tokopedia.shop.settings;

import android.content.Context;
import android.content.Intent;

import com.tokopedia.shop.settings.address.view.ShopSettingsAddressActivity;
import com.tokopedia.shop.settings.basicinfo.view.activity.ShopSettingsInfoActivity;
import com.tokopedia.shop.settings.etalase.view.activity.ShopSettingsEtalaseActivity;
import com.tokopedia.shop.settings.notes.view.activity.ShopSettingsNotesActivity;

/**
 * Created by hendry on 03/09/18.
 */
public class ShopSettingsInternalRouter {
    public static Intent getShopSettingsLocationActivity(Context context) {
        return ShopSettingsAddressActivity.createIntent(context);
    }

    public static Intent getShopSettingsBasicInfoActivity(Context context) {
        return ShopSettingsInfoActivity.createIntent(context);
    }

    public static Intent getShopSettingsEtalaseActivity(Context context) {
        return ShopSettingsEtalaseActivity.createIntent(context);
    }

    public static Intent getShopSettingsNotesActivity(Context context) {
        return ShopSettingsNotesActivity.createIntent(context);
    }
}
