package com.tokopedia.affiliate.feature.explore.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.affiliate.feature.explore.view.fragment.ExploreFragment;
import com.tokopedia.applink.ApplinkConst;

/**
 * @author by yfsx on 24/09/18.
 */
public class ExploreActivity extends BaseSimpleActivity {

    @DeepLink(ApplinkConst.AFFILIATE_EXPLORE)
    public static Intent getInstance(Context context) {
        Intent intent = new Intent(context, ExploreActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        return ExploreFragment.getInstance(getIntent().getExtras());
    }
}
