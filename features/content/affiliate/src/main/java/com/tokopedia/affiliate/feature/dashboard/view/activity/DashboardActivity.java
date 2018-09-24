package com.tokopedia.affiliate.feature.dashboard.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.airbnb.deeplinkdispatch.DeepLinkModule;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.affiliate.feature.dashboard.view.fragment.DashboardFragment;
import com.tokopedia.applink.ApplinkConst;

/**
 * @author by yfsx on 13/09/18.
 */
public class DashboardActivity extends BaseSimpleActivity {

    @DeepLink(ApplinkConst.AFFILIATE_DASHBOARD)
    public static Intent getInstance(Context context) {
        Intent intent = new Intent(context, DashboardActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        return DashboardFragment.getInstance(getIntent().getExtras());
    }
}
