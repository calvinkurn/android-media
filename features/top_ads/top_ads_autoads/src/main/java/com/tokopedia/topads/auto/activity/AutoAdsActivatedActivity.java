package com.tokopedia.topads.auto.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.topads.auto.fragment.AutoAdsActivatedFragment;

/**
 * Author errysuprayogi on 07,May,2019
 */
public class AutoAdsActivatedActivity extends BaseSimpleActivity {


    @Override
    protected Fragment getNewFragment() {
        return AutoAdsActivatedFragment.newInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar.setElevation(0f);
    }
}
