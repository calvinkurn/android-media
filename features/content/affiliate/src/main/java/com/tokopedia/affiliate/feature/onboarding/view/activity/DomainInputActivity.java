package com.tokopedia.affiliate.feature.onboarding.view.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.affiliate.feature.onboarding.view.fragment.DomainInputFragment;

public class DomainInputActivity extends BaseSimpleActivity {

    public static Intent createIntent(Context context) {
        return new Intent(context, DomainInputActivity.class);
    }
    @Override
    protected Fragment getNewFragment() {
        return DomainInputFragment.newInstance();
    }
}
