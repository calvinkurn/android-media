package com.tokopedia.shop.settings.edit.view;

import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;

/**
 * Created by Zulfikar on 5/19/2016.
 */
public class ShopSettingsInfoActivity extends BaseSimpleActivity{

    @Override
    protected Fragment getNewFragment() {
        return ShopSettingsInfoFragment.newInstance();
    }

}
