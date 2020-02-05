package com.tokopedia.shop;

import android.app.Activity;

import androidx.fragment.app.Fragment;

/**
 * Created by normansyahputa on 2/13/18.
 */

public interface ShopModuleRouter {

    void goToShareShop(Activity activity, String shopId, String shopUrl, String shareLabel);

    Fragment getReviewFragment(Activity activity, String shopId, String shopDomain);

}
