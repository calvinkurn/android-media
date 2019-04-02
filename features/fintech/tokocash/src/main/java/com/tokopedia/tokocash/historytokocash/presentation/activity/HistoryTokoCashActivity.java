package com.tokopedia.tokocash.historytokocash.presentation.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.tokocash.ApplinkConstant;
import com.tokopedia.tokocash.R;
import com.tokopedia.tokocash.historytokocash.presentation.fragment.HistoryTokoCashFragment;

/**
 * Created by nabillasabbaha on 2/5/18.
 */

public class HistoryTokoCashActivity extends BaseSimpleActivity {

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
        updateTitle(getString(R.string.title_menu_history_tokocash));
    }

    @Override
    protected Fragment getNewFragment() {
        return HistoryTokoCashFragment.newInstance();
    }
}
