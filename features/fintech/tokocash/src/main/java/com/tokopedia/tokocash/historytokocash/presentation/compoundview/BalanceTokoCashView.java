package com.tokopedia.tokocash.historytokocash.presentation.compoundview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.tokocash.R;
import com.tokopedia.tokocash.balance.view.BalanceTokoCash;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;

/**
 * Created by nabillasabbaha on 8/18/17.
 */

public class BalanceTokoCashView extends FrameLayout {

    private TextView balance;
    private TextView holdBalance;
    private ImageView tooltip;
    private RelativeLayout layoutHoldBalance;

    private ActionListener listener;

    public BalanceTokoCashView(Context context) {
        super(context);
        init(context);
    }

    public BalanceTokoCashView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BalanceTokoCashView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setListener(ActionListener listener) {
        this.listener = listener;
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.view_tokocash_balance, this, true);
        balance = view.findViewById(R.id.balance_tokocash);
        holdBalance = view.findViewById(R.id.hold_balance);
        ((TextView)view.findViewById(R.id.text_balance_tokocash)).setCompoundDrawablesWithIntrinsicBounds(MethodChecker.getDrawable
                (view.getContext(), R.drawable.ic_tokocash_icon), null, null , null);
        tooltip = view.findViewById(R.id.tooltip_tokocash);
        layoutHoldBalance = view.findViewById(R.id.layout_hold_balance);
    }

    public void renderDataBalance(BalanceTokoCash balanceTokoCash) {
        balance.setText(balanceTokoCash.getBalance());
        holdBalance.setText(balanceTokoCash.getHoldBalance());
        tooltip.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.showTooltipHoldBalance();
            }
        });
        layoutHoldBalance.setVisibility(balanceTokoCash.getRawHoldBalance() > 0 ? VISIBLE : GONE);
    }

    public interface ActionListener {
        void showTooltipHoldBalance();
    }
}
