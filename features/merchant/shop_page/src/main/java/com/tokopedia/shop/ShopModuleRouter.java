package com.tokopedia.shop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

/**
 * Created by normansyahputa on 2/13/18.
 */

public interface ShopModuleRouter {

    Fragment getKolPostShopFragment(String shopId, String createPostUrl);

    void goToShareShop(Activity activity, String shopId, String shopUrl, String shareLabel);
}
