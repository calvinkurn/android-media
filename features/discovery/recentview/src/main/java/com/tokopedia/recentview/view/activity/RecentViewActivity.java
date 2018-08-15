package com.tokopedia.recentview.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.recentview.view.fragment.RecentViewFragment;

/**
 * @author by nisie on 7/4/17.
 */

public class RecentViewActivity extends BaseSimpleActivity {

    @DeepLink(ApplinkConst.RECENT_VIEW)
    public static Intent getRecentViewApplinkIntent(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        return newInstance(context)
                .setData(uri.build())
                .putExtras(extras);
    }

    public static Intent newInstance(Context context) {
        return new Intent(context, RecentViewActivity.class);
    }

    @Override
    protected void onStart() {
        super.onStart();
        UnifyTracking.eventFeedViewAll();
    }

    @Override
    protected Fragment getNewFragment() {

        Bundle bundle = new Bundle();
        if (getIntent().getExtras() != null) {
            bundle.putAll(getIntent().getExtras());
        }
        return RecentViewFragment.createInstance(bundle);
    }
}
