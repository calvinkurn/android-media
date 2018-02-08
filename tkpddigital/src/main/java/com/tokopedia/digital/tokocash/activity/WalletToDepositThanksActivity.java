package com.tokopedia.digital.tokocash.activity;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.digital.R;
import com.tokopedia.digital.tokocash.fragment.WalletToDepositThanksFragment;
import com.tokopedia.digital.tokocash.model.WalletToDepositThanksPassData;

/**
 * @author anggaprasetiyo on 8/21/17.
 */

public class WalletToDepositThanksActivity extends BasePresenterActivity
        implements WalletToDepositThanksFragment.ActionListener {
    private static final String EXTRA_WALLET_TO_DEPOSIT_THANKS_PASS_DATA =
            "EXTRA_WALLET_TO_DEPOSIT_THANKS_PASS_DATA";

    public static final int RESULT_BACK_SUCCESS = 4;
    public static final int RESULT_BACK_RETRY = 5;
    public static final int RESULT_BACK_FAILED = 6;
    public static final int REQUEST_CODE = WalletToDepositThanksActivity.class.hashCode();

    private WalletToDepositThanksPassData passData;

    public static Intent newInstance(Context context, WalletToDepositThanksPassData passData) {
        return new Intent(context, WalletToDepositThanksActivity.class)
                .putExtra(EXTRA_WALLET_TO_DEPOSIT_THANKS_PASS_DATA, passData);
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        this.passData = extras.getParcelable(EXTRA_WALLET_TO_DEPOSIT_THANKS_PASS_DATA);
    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_wallet_to_deposit_thanks_digital_module;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void setViewListener() {
        Fragment fragment = getFragmentManager().findFragmentById(R.id.container);
        if (fragment == null || !(fragment instanceof WalletToDepositThanksFragment))
            getFragmentManager().beginTransaction().replace(R.id.container,
                    WalletToDepositThanksFragment.newInstance(passData)).commit();
    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void onBackToTokoCashClicked(boolean isSucceeded) {
        setResult(isSucceeded ? RESULT_BACK_SUCCESS : RESULT_BACK_FAILED);
        finish();
    }

    @Override
    public void onRetryClicked() {
        setResult(RESULT_BACK_RETRY);
        finish();
    }

    @Override
    public void setTitlePage(String titlePage) {
        toolbar.setTitle(titlePage);
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }

    @Override
    protected void setupToolbar() {
        super.setupToolbar();
        toolbar = (Toolbar) findViewById(com.tokopedia.core.R.id.app_bar);
        toolbar.setTitle(getTitle());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);
    }

    @Override
    public void onBackPressed() {

    }
}
