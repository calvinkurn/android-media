package com.tokopedia.challenges.view.activity;

import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;

public class BaseActivity extends BaseSimpleActivity{

    @Override
    protected Fragment getNewFragment() {
        return null;
    }
}
