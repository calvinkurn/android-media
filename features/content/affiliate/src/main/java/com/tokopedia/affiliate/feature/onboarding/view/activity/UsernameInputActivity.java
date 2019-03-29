package com.tokopedia.affiliate.feature.onboarding.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.affiliate.feature.onboarding.view.fragment.UsernameInputFragment;
import com.tokopedia.applink.ApplinkConst;

public class UsernameInputActivity extends BaseSimpleActivity {
    public static final String PARAM_PRODUCT_ID = "product_id";

    @DeepLink(ApplinkConst.AFFILIATE_ONBOARDING)
    public static Intent createApplinkIntent(Context context, Bundle bundle) {
        String productId = bundle.getString(PARAM_PRODUCT_ID, "");
        Intent intent = createIntent(context, productId);
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent createIntent(Context context) {
        return createIntent(context, "");
    }

    public static Intent createIntent(Context context, String productId) {
        Intent intent = new Intent(context, UsernameInputActivity.class);
        intent.putExtra(PARAM_PRODUCT_ID, productId);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        Bundle bundle = new Bundle();
        if (getIntent().getExtras() != null) {
            bundle.putAll(getIntent().getExtras());
        }
        return UsernameInputFragment.newInstance(bundle);
    }
}
