package com.tokopedia.shop.settings;

import android.content.Context;

/**
 * Created by hendry on 07/08/18.
 */

public interface ShopSettingsRouter {
    void goToShopEditor(Context context);

    void goToManageShopEtalase(Context context);

    void goToManageShopNotes(Context context);

    void goToManageShopLocation(Context context);
}
