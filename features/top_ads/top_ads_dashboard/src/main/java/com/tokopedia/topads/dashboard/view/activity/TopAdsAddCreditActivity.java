package com.tokopedia.topads.dashboard.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsAddCreditFragment;

import java.util.List;

/**
 * Created by Nathaniel on 11/22/2016.
 */

public class TopAdsAddCreditActivity extends BaseSimpleActivity {

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, TopAdsAddCreditActivity.class);
    }

    @DeepLink(ApplinkConst.SellerApp.TOPADS_CREDIT)
    public static Intent getCallingApplinkIntent(Context context, Bundle extras) {
        if (GlobalConfig.isSellerApp()) {
            Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
            return getCallingIntent(context)
                    .setData(uri.build())
                    .putExtras(extras);
        } else {
            // TODO ROUTER
            return null /*ApplinkUtils.getSellerAppApplinkIntent(context, extras)*/;
        }
    }

    @Override
    protected Fragment getNewFragment() {
        return TopAdsAddCreditFragment.createInstance();
    }

    @Override
    public String getScreenName() {
        return null;
    }

    @Override
    public void onBackPressed() {
        if (getIntent().getExtras() != null && getIntent().getExtras().getBoolean(TopAdsDashboardConstant.EXTRA_APPLINK_FROM_PUSH, false)) {
            Intent homeIntent = null;
            // TODO ROUTER
            /*if (GlobalConfig.isSellerApp()) {
                homeIntent = SellerAppRouter.getSellerHomeActivity(this);
            } else {
                homeIntent = HomeRouter.getHomeActivity(this);
            }*/
            startActivity(homeIntent);
            finish();
        } else {
            super.onBackPressed();
        }
    }
}