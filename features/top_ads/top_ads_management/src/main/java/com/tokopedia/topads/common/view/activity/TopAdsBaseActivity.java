package com.tokopedia.topads.common.view.activity;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;

public class TopAdsBaseActivity extends BaseSimpleActivity {


    @Nullable
    @Override
    protected Fragment getNewFragment() {
        return null;
    }
}
