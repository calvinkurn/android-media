package com.tokopedia.digital.tokocash.compoundview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;
import com.tokopedia.digital.tokocash.model.tokocashitem.TokoCashBalanceData;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nabillasabbaha on 8/18/17.
 */

public class BalanceTokoCashView extends LinearLayout {

    @BindView(R2.id.balance_tokocash)
    TextView balance;
    @BindView(R2.id.hold_balance)
    TextView holdBalance;
    @BindView(R2.id.tooltip_tokocash)
    ImageView tooltip;
    @BindView(R2.id.layout_hold_balance)
    RelativeLayout layoutHoldBalance;

    private ActionListener listener;

    public BalanceTokoCashView(Context context) {
        super(context);
        init();
    }

    public BalanceTokoCashView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BalanceTokoCashView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setListener(ActionListener listener) {
        this.listener = listener;
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_tokocash_balance, this, true);
        ButterKnife.bind(this);
    }

    public void renderDataBalance(TokoCashBalanceData tokoCashBalanceData) {
        balance.setText(tokoCashBalanceData.getBalance());
        holdBalance.setText(tokoCashBalanceData.getHoldBalance());
        tooltip.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.showTooltipHoldBalance();
            }
        });
        layoutHoldBalance.setVisibility(tokoCashBalanceData.getRawHoldBalance() > 0 ? VISIBLE : GONE);
    }

    public interface ActionListener {
        void showTooltipHoldBalance();
    }
}
