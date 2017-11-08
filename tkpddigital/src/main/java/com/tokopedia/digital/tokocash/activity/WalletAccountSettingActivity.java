package com.tokopedia.digital.tokocash.activity;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.digital.R;
import com.tokopedia.digital.tokocash.fragment.WalletAccountSettingFragment;

/**
 * @author anggaprasetiyo on 8/24/17.
 */

public class WalletAccountSettingActivity extends BasePresenterActivity implements
        WalletAccountSettingFragment.ActionListener {
    private static final String EXTRA_WALLET_ACCOUNT_SETTING_PASS_DATA =
            "EXTRA_WALLET_ACCOUNT_SETTING_PASS_DATA";
    public static final int REQUEST_CODE = WalletAccountSettingActivity.class.hashCode();

    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, WalletAccountSettingActivity.class);
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
        toolbar.setTitle(getString(R.string.tokocash_title_account_setting));
    }

    @Override
    protected void setViewListener() {
        Fragment fragment = getFragmentManager().findFragmentById(R.id.container);
        if (fragment == null || !(fragment instanceof WalletAccountSettingFragment))
            getFragmentManager().beginTransaction().replace(R.id.container,
                    WalletAccountSettingFragment.newInstance()).commit();

    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void directPageToHome() {
        onBackPressed();
        setResult(RESULT_OK);
        finish();
    }
}
