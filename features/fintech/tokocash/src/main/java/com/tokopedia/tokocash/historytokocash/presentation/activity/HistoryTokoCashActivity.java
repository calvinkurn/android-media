package com.tokopedia.tokocash.historytokocash.presentation.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.tokocash.ApplinkConstant;
import com.tokopedia.tokocash.R;
import com.tokopedia.tokocash.TokoCashComponentInstance;
import com.tokopedia.tokocash.WalletUserSession;
import com.tokopedia.tokocash.activation.presentation.activity.ActivateTokoCashActivity;
import com.tokopedia.tokocash.di.TokoCashComponent;
import com.tokopedia.tokocash.historytokocash.presentation.fragment.HistoryTokoCashFragment;

import javax.inject.Inject;

/**
 * Created by nabillasabbaha on 2/5/18.
 */

public class HistoryTokoCashActivity extends BaseSimpleActivity {

    @Inject
    WalletUserSession walletUserSession;

    @SuppressWarnings("unused")
    @DeepLink(ApplinkConstant.WALLET_TRANSACTION_HISTORY)
    public static Intent getcallingIntent(Context context, Bundle extras) {
        return HistoryTokoCashActivity.newInstance(context);
    }

    public static Intent newInstance(Context context) {
        return new Intent(context, HistoryTokoCashActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initInjector();

        updateTitle(getString(R.string.title_menu_history_tokocash));

        if (isUserInactiveTokoCash()) {
            startActivity(ActivateTokoCashActivity.newInstance(getApplicationContext()));
            finish();
        }
    }

    private boolean isUserInactiveTokoCash() {
        return walletUserSession != null && walletUserSession.getTokenWallet().equals("");
    }

    private void initInjector() {
        TokoCashComponent tokoCashComponent =
                TokoCashComponentInstance.getComponent(getApplication());
        tokoCashComponent.inject(this);
    }

    @Override
    protected Fragment getNewFragment() {
        return HistoryTokoCashFragment.newInstance();
    }
}
