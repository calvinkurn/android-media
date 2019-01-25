package com.tokopedia.saldodetails.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.saldodetails.view.fragment.SaldoIntroFragment;

public class SaldoIntroActivity extends BaseSimpleActivity {

    @SuppressWarnings("unused")
    @DeepLink(ApplinkConst.SALDO_INTRO)
    public static Intent getcallingIntent(Context context, Bundle extras) {
        return SaldoIntroActivity.newInstance(context);
    }

    public static Intent newInstance(Context context) {
        return new Intent(context, SaldoIntroActivity.class);
    }

    @Override
    protected Fragment getNewFragment() {
        return SaldoIntroFragment.newInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCloseButton();
    }

    private void setCloseButton() {
        if (getSupportActionBar() == null) {
            return;
        }
        getSupportActionBar().setHomeAsUpIndicator(ContextCompat.getDrawable(this,
                com.tokopedia.abstraction.R.drawable.ic_close_default));
    }
}

