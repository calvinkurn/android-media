package com.tokopedia.tokocash.historytokocash.presentation.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.tokocash.R;
import com.tokopedia.tokocash.historytokocash.presentation.fragment.WaitingTransactionFragment;
import com.tokopedia.tokocash.historytokocash.presentation.model.TokoCashHistoryData;

/**
 * Created by nabillasabbaha on 2/13/18.
 */

public class WaitingTransactionActivity extends BaseSimpleActivity {

    public static Intent newInstance(Context context, TokoCashHistoryData tokoCashHistoryData) {
        Intent intent = new Intent(context, WaitingTransactionActivity.class);
        intent.putExtra(WaitingTransactionFragment.TOKOCASH_HISTORY_DATA_EXTRA, tokoCashHistoryData);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateTitle(getString(R.string.title_waiting_transaction_tokocash));
    }

    @Override
    protected Fragment getNewFragment() {
        return WaitingTransactionFragment.newInstance((TokoCashHistoryData) getIntent()
                .getParcelableExtra(WaitingTransactionFragment.TOKOCASH_HISTORY_DATA_EXTRA));
    }
}
