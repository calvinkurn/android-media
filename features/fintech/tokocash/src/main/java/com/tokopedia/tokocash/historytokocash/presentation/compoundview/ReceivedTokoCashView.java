package com.tokopedia.tokocash.historytokocash.presentation.compoundview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokopedia.tokocash.R;
import com.tokopedia.tokocash.qrpayment.presentation.model.BalanceTokoCash;

/**
 * Created by nabillasabbaha on 8/18/17.
 */

public class ReceivedTokoCashView extends FrameLayout {

    private static String THRESHOLD_DEFAULT = "Rp 20.000.000";
    private static long MAX_TOTAL_BALANCE = 20000000;
    private static int PROGRESS_DUMMY = 30;
    private static int PROGRESS_COMPLETE = 100;
    private static int PROGRESS_ZERO = 0;

    private TextView incomeTokocash;
    private ProgressBar progressBarTokocash;
    private TextView limitTokocash;

    private long rawThreshold;
    private String threshold;

    public ReceivedTokoCashView(Context context) {
        super(context);
        init(context);
    }

    public ReceivedTokoCashView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ReceivedTokoCashView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.view_tokocash_received, this, true);
        incomeTokocash = view.findViewById(R.id.income_tokocash);
        progressBarTokocash = view.findViewById(R.id.progress_bar_tokocash_balance);
        limitTokocash = view.findViewById(R.id.limit_tokocash);
    }

    public void renderReceivedView(BalanceTokoCash tokoCashBalanceData) {
        incomeTokocash.setText(tokoCashBalanceData.getTotalBalance());
        renderLimitTokoCash(tokoCashBalanceData);
    }

    private void renderLimitTokoCash(BalanceTokoCash tokoCashBalanceData) {
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
        int progress = (int) ((totalBalance * PROGRESS_COMPLETE) / rawThreshold);
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