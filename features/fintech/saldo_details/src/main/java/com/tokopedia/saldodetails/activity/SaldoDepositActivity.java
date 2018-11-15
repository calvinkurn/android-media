package com.tokopedia.saldodetails.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.saldodetails.R;
import com.tokopedia.saldodetails.di.SaldoDetailsComponent;
import com.tokopedia.saldodetails.di.SaldoDetailsComponentInstance;
import com.tokopedia.saldodetails.presenter.SaldoDetailsPresenter;
import com.tokopedia.saldodetails.view.fragment.SaldoDepositFragment;

@DeepLink(ApplinkConst.DEPOSIT)
public class SaldoDepositActivity extends BaseSimpleActivity implements
        HasComponent<SaldoDetailsComponent> {

    private static final String TAG = "DEPOSIT_FRAGMENT";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SaldoDetailsPresenter.REQUEST_WITHDRAW_CODE && resultCode == Activity.RESULT_OK) {
            ((SaldoDepositFragment) getSupportFragmentManager().findFragmentByTag(TAG)).refresh();
        }
    }

    @Override
    public SaldoDetailsComponent getComponent() {
        return SaldoDetailsComponentInstance.getComponent(getApplication());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected Fragment getNewFragment() {
        return SaldoDepositFragment.createInstance();
    }

    @Override
    protected void setupLayout(Bundle savedInstanceState) {
        super.setupLayout(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.white));
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }
}
