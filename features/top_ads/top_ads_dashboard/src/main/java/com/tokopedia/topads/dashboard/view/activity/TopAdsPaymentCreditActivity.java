package com.tokopedia.topads.dashboard.view.activity;

import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsPaymentCreditFragment;

/**
 * Created by Nathaniel on 11/22/2016.
 */

public class TopAdsPaymentCreditActivity extends BaseSimpleActivity {

    @Override
    public String getScreenName() {
        return null;
    }

    @Override
    protected Fragment getNewFragment() {
        return TopAdsPaymentCreditFragment.createInstance();
    }

    @Override
    protected String getTagFragment() {
        return TopAdsPaymentCreditFragment.class.getSimpleName();
    }
}