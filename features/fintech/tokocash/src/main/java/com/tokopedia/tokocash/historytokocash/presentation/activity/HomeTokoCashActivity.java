package com.tokopedia.tokocash.historytokocash.presentation.activity;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.tokocash.ApplinkConstant;
import com.tokopedia.tokocash.R;
import com.tokopedia.tokocash.TokoCashRouter;
import com.tokopedia.tokocash.accountsetting.presentation.activity.AccountSettingActivity;
import com.tokopedia.tokocash.historytokocash.presentation.fragment.HomeTokoCashFragment;
import com.tokopedia.tokocash.network.api.WalletUrl;

/**
 * Created by nabillasabbaha on 2/5/18.
 */

public class HomeTokoCashActivity extends BaseSimpleActivity
        implements HomeTokoCashFragment.ActionListener {

    public static final String EXTRA_TOP_UP_AVAILABLE = "EXTRA_TOP_UP_AVAILABLE";

    @SuppressWarnings("unused")
    @DeepLink(ApplinkConstant.WALLET_HOME)
    public static Intent getcallingIntent(Context context, Bundle extras) {
        boolean topUpVisible = Boolean.parseBoolean(
                extras.getString(ApplinkConstant.WALLET_TOP_UP_VISIBILITY, "false"));
        return HomeTokoCashActivity.newInstance(context, topUpVisible);
    }

    private static Intent newInstance(Context context, boolean topUpAvailable) {
        return new Intent(context, HomeTokoCashActivity.class)
                .putExtra(EXTRA_TOP_UP_AVAILABLE, topUpAvailable);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Fragment getNewFragment() {
        return HomeTokoCashFragment.newInstance(getIntent().getBooleanExtra(EXTRA_TOP_UP_AVAILABLE, false));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_digital_tokocash, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_menu_history_tokocash) {
            startActivity(HistoryTokoCashActivity.newInstance(getApplicationContext()));
            return true;
        } else if (item.getItemId() == R.id.action_menu_help_tokocash) {
            Application application = this.getApplication();
            if (application != null && application instanceof TokoCashRouter) {
                Intent intent = ((TokoCashRouter) application).getWebviewActivityWithIntent(this,
                WalletUrl.BaseUrl.WEB_DOMAIN + WalletUrl.Wallet.WEBVIEW_HELP_CENTER, getString(R.string.title_help_history));
                startActivity(intent);
            }
            return true;
        } else if (item.getItemId() == R.id.action_account_setting_tokocash) {
            startActivityForResult(AccountSettingActivity.newInstance(this),
                    AccountSettingActivity.REQUEST_CODE);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case AccountSettingActivity.REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK && data != null &&
                        data.hasExtra(AccountSettingActivity.KEY_INTENT_RESULT)) {
                    setResult(RESULT_OK);
                    finish();
                }
                break;
        }
    }

    @Override
    public void setTitle(String title) {
        updateTitle(title);
    }
}