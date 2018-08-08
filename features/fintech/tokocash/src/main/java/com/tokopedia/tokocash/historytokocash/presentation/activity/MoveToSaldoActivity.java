package com.tokopedia.tokocash.historytokocash.presentation.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.tokocash.R;
import com.tokopedia.tokocash.historytokocash.presentation.fragment.MoveToSaldoFragment;
import com.tokopedia.tokocash.historytokocash.presentation.model.WalletToDepositPassData;

/**
 * Created by nabillasabbaha on 2/19/18.
 */

public class MoveToSaldoActivity extends BaseSimpleActivity implements
        MoveToSaldoFragment.ActionListener {

    private static final String WALLET_TO_DEPOSIT_PASS_DATA = "WALLET_TO_DEPOSIT_PASS_DATA";
    public static final int RESULT_WALLET_TO_DEPOSIT_SUCCESS = 4;
    public static final int RESULT_WALLET_TO_DEPOSIT_FAILED = 6;

    public static Intent newInstance(Context context, WalletToDepositPassData walletToDepositPassData) {
        Intent intent = new Intent(context, MoveToSaldoActivity.class);
        intent.putExtra(WALLET_TO_DEPOSIT_PASS_DATA, walletToDepositPassData);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateTitle(getString(R.string.title_help_history));
    }

    @Override
    protected Fragment getNewFragment() {
        return MoveToSaldoFragment.newInstance((WalletToDepositPassData) getIntent().getParcelableExtra(WALLET_TO_DEPOSIT_PASS_DATA));
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0)
            getFragmentManager().popBackStackImmediate();
        else
            super.onBackPressed();
    }

    @Override
    public void setTitlePage(String titlePage) {
        updateTitle(titlePage);
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
}