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
import com.tokopedia.digital.tokocash.model.tokocashitem.TokoCashData;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nabillasabbaha on 8/18/17.
 */

public class ReceivedTokoCashView extends LinearLayout {

    @BindView(R2.id.income_tokocash)
    TextView incomeTokocash;
    @BindView(R2.id.progress_bar_tokocash)
    ProgressBar progressBar;
    @BindView(R2.id.limit_tokocash)
    TextView limitTokocash;

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

    public void renderReceivedView(TokoCashData tokoCashData) {
        incomeTokocash.setText(tokoCashData.getTotalBalance());
        progressBar.setProgress(50);
        limitTokocash.setText("Limit: " + "Rp 20.000.000" + "/bulan");
    }
}
