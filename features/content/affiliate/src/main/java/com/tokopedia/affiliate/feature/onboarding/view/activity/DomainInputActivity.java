package com.tokopedia.affiliate.feature.onboarding.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.affiliate.feature.onboarding.view.fragment.DomainInputFragment;

public class DomainInputActivity extends BaseSimpleActivity {
    public static final String PARAM_PRODUCT_ID = "product_id";

    public static Intent createIntent(Context context) {
        return createIntent(context, "");
    }

    public static Intent createIntent(Context context, String productId) {
        Intent intent = new Intent(context, DomainInputActivity.class);
        intent.putExtra(PARAM_PRODUCT_ID, productId);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        Bundle bundle = new Bundle();
        if (getIntent().getExtras() != null) {
            bundle.putAll(getIntent().getExtras());
        }
        return DomainInputFragment.newInstance(bundle);
    }
}
