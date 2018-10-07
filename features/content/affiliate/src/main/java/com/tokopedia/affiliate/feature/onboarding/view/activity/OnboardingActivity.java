package com.tokopedia.affiliate.feature.onboarding.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.affiliate.feature.onboarding.view.fragment.OnboardingFragment;
import com.tokopedia.applink.ApplinkConst;

public class OnboardingActivity extends BaseSimpleActivity {

    public static final String PARAM_PRODUCT_ID = "product_id";
    public static final String PARAM_IS_FINISH = "is_finish";
    public static final Boolean FINISH_TRUE = true;

    @DeepLink(ApplinkConst.AFFILIATE_ONBOARDING)
    public static Intent createApplinkIntent(Context context, Bundle bundle) {
        Intent intent = createIntent(context, false);
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent createIntent(Context context) {
        return createIntent(context, false);
    }

    public static Intent createIntent(Context context, Boolean isFinish) {
        Intent intent = new Intent(context, OnboardingActivity.class);
        intent.putExtra(PARAM_IS_FINISH, isFinish);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        Bundle bundle = new Bundle();
        if (getIntent().getExtras() != null) {
            bundle.putAll(getIntent().getExtras());
        }
        return OnboardingFragment.newInstance(bundle);
    }
}
