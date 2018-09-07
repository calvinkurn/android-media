package com.tokopedia.tokocash.historytokocash.presentation.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.tokocash.R;
import com.tokopedia.tokocash.historytokocash.presentation.fragment.ThankYouMoveToSaldoFragment;
import com.tokopedia.tokocash.historytokocash.presentation.model.WalletToDepositThanksPassData;

/**
 * Created by nabillasabbaha on 2/19/18.
 */

public class ThankYouMoveToSaldoActivity extends BaseSimpleActivity
        implements ThankYouMoveToSaldoFragment.ActionListener {

    private static final String EXTRA_WALLET_TO_DEPOSIT_THANKS_PASS_DATA =
            "EXTRA_WALLET_TO_DEPOSIT_THANKS_PASS_DATA";
    public static final int REQUEST_CODE = 143;

    public static final int RESULT_BACK_SUCCESS = 4;
    public static final int RESULT_BACK_RETRY = 5;
    public static final int RESULT_BACK_FAILED = 6;

    public static Intent newInstance(Context context, WalletToDepositThanksPassData walletToDepositThanksPassData) {
        Intent intent = new Intent(context, ThankYouMoveToSaldoActivity.class);
        intent.putExtra(EXTRA_WALLET_TO_DEPOSIT_THANKS_PASS_DATA, walletToDepositThanksPassData);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateTitle(getString(R.string.title_help_history));
    }

    @Override
    protected Fragment getNewFragment() {
        return ThankYouMoveToSaldoFragment.newInstance((WalletToDepositThanksPassData)
                getIntent().getParcelableExtra(EXTRA_WALLET_TO_DEPOSIT_THANKS_PASS_DATA));
    }

    @Override
    public void onBackToTokoCashClicked(boolean isSucceeded) {
        setResult(isSucceeded ? RESULT_BACK_SUCCESS : RESULT_BACK_FAILED);
        finish();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.toolbar_no_icon_back;
    }

    @Override
    public void onRetryClicked() {
        setResult(RESULT_BACK_RETRY);
        finish();
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void setupLayout(Bundle savedInstanceState) {
        super.setupLayout(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }

    @Override
    public void setTitlePage(String titlePage) {
        updateTitle(titlePage);
    }
}