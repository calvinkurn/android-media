package com.tokopedia.core.shop.presenter;

import android.content.Intent;
import android.support.v4.app.Fragment;

/**
 * Created by Toped18 on 5/19/2016.
 */
public interface ShopSettingView {

    String FRAGMENT_TO_SHOW = "FragmentToShow";

    String EDIT_SHOP_FRAGMENT_TAG = "EditShopFragment";
    String CREATE_SHOP_FRAGMENT_TAG = "CreateShopFragment";

    String ON_BACK = "ON_BACK";
    String LOG_OUT = "LOG_OUT";
    String FINISH = "FINISH";

    void initFragment(String FRAGMENT_TAG);
    void moveToFragment(Fragment fragment, boolean isAddtoBackStack, String TAG);
    void fetchExtras(Intent intent);
    boolean isFragmentCreated(String tag);
}

