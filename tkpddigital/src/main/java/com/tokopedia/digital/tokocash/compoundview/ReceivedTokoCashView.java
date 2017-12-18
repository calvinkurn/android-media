package com.tokopedia.digital.tokocash.compoundview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;
import com.tokopedia.digital.tokocash.model.tokocashitem.TokoCashBalanceData;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nabillasabbaha on 8/18/17.
 */

public class ReceivedTokoCashView extends LinearLayout {

    private static String THRESHOLD_DEFAULT = "Rp 20.000.000";
    private static long MAX_TOTAL_BALANCE = 20000000;
    private static int PROGRESS_DUMMY = 30;
    private static int PROGRESS_COMPLETE = 100;
    private static int PROGRESS_ZERO = 0;

    @BindView(R2.id.income_tokocash)
    TextView incomeTokocash;
    @BindView(R2.id.progress_bar_tokocash_balance)
    ProgressBar progressBarTokocash;
    @BindView(R2.id.limit_tokocash)
    TextView limitTokocash;

    private long rawThreshold;
    private String threshold;

    public ReceivedTokoCashView(Context context) {
        super(context);
        init();
    }

    public ReceivedTokoCashView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ReceivedTokoCashView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_tokocash_received, this, true);
        ButterKnife.bind(this);
    }

    public void renderReceivedView(TokoCashBalanceData tokoCashBalanceData) {
        incomeTokocash.setText(tokoCashBalanceData.getTotalBalance());
        renderLimitTokoCash(tokoCashBalanceData);
    }

    private void renderLimitTokoCash(TokoCashBalanceData tokoCashBalanceData) {
        if (tokoCashBalanceData.getThreshold() != null) {
            rawThreshold = tokoCashBalanceData.getRawThreshold();
            threshold = tokoCashBalanceData.getThreshold();
        } else {
            rawThreshold = MAX_TOTAL_BALANCE;
            threshold = THRESHOLD_DEFAULT;
        }
        limitTokocash.setText(String.format(getContext().getString(R.string.limit_balance_tokocash), threshold));
        renderProgressBarTokoCash(tokoCashBalanceData.getRawTotalBalance());
    }

    private void renderProgressBarTokoCash(long totalBalance) {
        int progress = (int) ((totalBalance * PROGRESS_COMPLETE)/ rawThreshold);
        progressBarTokocash.setMax(PROGRESS_COMPLETE);
        progressBarTokocash.setProgress(PROGRESS_DUMMY);
        if (progress == PROGRESS_ZERO) {
            progressBarTokocash.setMax(PROGRESS_ZERO);
            progressBarTokocash.setProgress(PROGRESS_ZERO);
        } else {
            progressBarTokocash.setMax(PROGRESS_COMPLETE);
            progressBarTokocash.setProgress(progress);
        }
    }
}