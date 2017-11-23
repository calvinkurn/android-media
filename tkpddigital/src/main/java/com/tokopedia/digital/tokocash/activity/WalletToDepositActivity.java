package com.tokopedia.digital.tokocash.activity;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.digital.R;
import com.tokopedia.digital.tokocash.fragment.WalletToDepositFragment;
import com.tokopedia.digital.tokocash.model.WalletToDepositPassData;

/**
 * @author anggaprasetiyo on 8/21/17.
 */

public class WalletToDepositActivity extends BasePresenterActivity
        implements WalletToDepositFragment.ActionListener {
    private static final String EXTRA_WALLET_TO_DEPOSIT_PASS_DATA = "EXTRA_WALLET_TO_DEPOSIT_PASS_DATA";
    public static final int REQUEST_CODE = WalletToDepositActivity.class.hashCode();
    public static final int RESULT_WALLET_TO_DEPOSIT_SUCCESS = 4;
    public static final int RESULT_WALLET_TO_DEPOSIT_FAILED = 6;
    public static final String STATE_TITLE_PAGE = "STATE_TITLE_PAGE";

    private WalletToDepositPassData passData;
    private String stateTitlePage;

    public static Intent newInstance(Context context, WalletToDepositPassData passData) {
        return new Intent(context, WalletToDepositActivity.class)
                .putExtra(EXTRA_WALLET_TO_DEPOSIT_PASS_DATA, passData);
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        passData = extras.getParcelable(EXTRA_WALLET_TO_DEPOSIT_PASS_DATA);
        if (passData != null) stateTitlePage = passData.getTitle();
    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_wallet_to_deposit_digital_module;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void setViewListener() {
        Fragment fragment = getFragmentManager().findFragmentById(R.id.container);
        if (fragment == null || !(fragment instanceof WalletToDepositFragment))
            getFragmentManager().beginTransaction().replace(R.id.container,
                    WalletToDepositFragment.newInstance(passData)).commit();
    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void setTitlePage(String titlePage) {
        this.stateTitlePage = titlePage;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (stateTitlePage != null) toolbar.setTitle(stateTitlePage);
    }

    @Override
    public void onWalletToDepositSucceeded() {
        Intent backIntent = new Intent();
        setResult(RESULT_WALLET_TO_DEPOSIT_SUCCESS, backIntent);
        finish();
    }

    @Override
    public void onWalletToDepositFailed() {
        Intent backIntent = new Intent();
        setResult(RESULT_WALLET_TO_DEPOSIT_FAILED, backIntent);
        finish();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(STATE_TITLE_PAGE, stateTitlePage);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        stateTitlePage = savedInstanceState.getString(STATE_TITLE_PAGE);
    }
}
