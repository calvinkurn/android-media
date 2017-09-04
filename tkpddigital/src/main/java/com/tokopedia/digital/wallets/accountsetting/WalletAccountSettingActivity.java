package com.tokopedia.digital.wallets.accountsetting;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.digital.R;
import com.tokopedia.digital.base.BaseDigitalPresenterActivity;

/**
 * @author anggaprasetiyo on 8/24/17.
 */

public class WalletAccountSettingActivity extends BaseDigitalPresenterActivity {
    private static final String EXTRA_WALLET_ACCOUNT_SETTING_PASS_DATA =
            "EXTRA_WALLET_ACCOUNT_SETTING_PASS_DATA";
    private WalletAccountSettingPassData walletAccountSettingPassData;

    public static Intent newInstance(Context context, WalletAccountSettingPassData passData) {
        Intent intent = new Intent(context, WalletAccountSettingActivity.class);
        intent.putExtra(EXTRA_WALLET_ACCOUNT_SETTING_PASS_DATA, passData);
        return intent;
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
        walletAccountSettingPassData = extras.getParcelable(EXTRA_WALLET_ACCOUNT_SETTING_PASS_DATA);
    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_with_fragment_container_digital_module;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void setViewListener() {
        Fragment fragment = getFragmentManager().findFragmentById(R.id.container);
        if (fragment == null || !(fragment instanceof WalletAccountSettingFragment))
            getFragmentManager().beginTransaction().replace(R.id.container,
                    WalletAccountSettingFragment.newInstance(walletAccountSettingPassData)).commit();

    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {

    }
}
