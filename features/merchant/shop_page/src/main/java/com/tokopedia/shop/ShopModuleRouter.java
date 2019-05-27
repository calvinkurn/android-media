package com.tokopedia.shop;

import android.app.Activity;

/**
 * Created by normansyahputa on 2/13/18.
 */

public interface ShopModuleRouter {

    void goToShareShop(Activity activity, String shopId, String shopUrl, String shareLabel);
}
