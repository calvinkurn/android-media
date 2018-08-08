package com.tokopedia.shop.settings.address.view;

import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;

public class ShopSettingsAddressActivity extends BaseSimpleActivity {

    @Override
    protected Fragment getNewFragment() {
        return ShopSettingsAddressFragment.newInstance();
    }
}
