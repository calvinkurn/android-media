package com.tokopedia.shop.settings.basicinfo.view.activity;

import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.shop.settings.basicinfo.view.fragment.ShopSettingsInfoFragment;

/**
 * Created by Zulfikar on 5/19/2016.
 */
public class ShopSettingsInfoActivity extends BaseSimpleActivity {

    @Override
    protected Fragment getNewFragment() {
        return ShopSettingsInfoFragment.newInstance();
    }

}
